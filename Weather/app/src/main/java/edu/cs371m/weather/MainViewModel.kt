package edu.cs371m.weather

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import edu.cs371m.weather.api.Repository
import edu.cs371m.weather.api.Repository2
import edu.cs371m.weather.api.WeatherApi
import edu.cs371m.weather.api.WeatherApi2
import edu.cs371m.weather.ui.main.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import edu.cs371m.weather.ui.main.weatherRepository2_key
import edu.cs371m.weather.ui.main.weatherRepository_key
import kotlinx.android.synthetic.main.main_fragment.*

class MainViewModel : ViewModel() {
    // XXX You need some important member variables
    private val weatherapi = WeatherApi.create()
    private val weatherapi2 = WeatherApi2.create()
    private val weatherRepository = Repository(weatherapi)
    private val weatherRepository2 = Repository2(weatherapi2)
    private val weatherMaxTemp = MutableLiveData<Double>()
    private val weatherMinTemp = MutableLiveData<Double>()
    private val humidity = MutableLiveData<Int>()
    private var location = MutableLiveData<String>()
    private var user_theme = MutableLiveData<String>().apply {
        value = "beach"
    }
    var sourceSelect = MutableLiveData<String>()
    private var sourceRes1 = MutableLiveData<WeatherApi.Main>()
    private var sourceRes2 = MutableLiveData<WeatherApi2.subRes>()
    private var favlist = MutableLiveData<List<String>>().apply {
        value = mutableListOf()
    }
    private var source_weight = MutableLiveData<List<Int>>().apply {
        value = mutableListOf(1,1)
    }
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val username = FirebaseAuth.getInstance().currentUser?.email
    val source_list = mutableListOf("openapi", "hgbrasil", "mix")


    init {
        // XXX one-liner to kick off the app
        if (location.value == null) {
            netRefresh("London", source_list[0])
        } else {
            netRefresh((location.value).toString(), source_list[0])
        }

        db.collection("preference")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == username){
                        var _source_weight = document.data["sourcew"] as ArrayList<Int>
                        for (i in _source_weight.indices){
                            updateSW(i, _source_weight[i])
                        }
                        updateTheme(document.data["theme"] as String)
                        var fav_arr = document.data["favorite"] as ArrayList<*>
                        for (i in fav_arr.indices){
                            addtoFav(fav_arr[i].toString())
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
            }
    }
    fun setLocation(location_input: String) {
        location.value = location_input
    }
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun netRefresh(location_input: String, source: String) {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO) {
            // Update LiveData from IO dispatcher, use postValue
            if (source==source_list[0]) {
                var tmp = weatherRepository.getWeather(
                    weatherRepository_key,
                    "metric",
                    location_input
                ).execute().body()?.main

                if (tmp != null) {
                    weatherMaxTemp.postValue("%.2f".format(tmp.temp_max).toDouble())
                    weatherMinTemp.postValue(tmp.temp_min)
                    humidity.postValue(tmp.humidity)
                }
            }
            else if (source==source_list[1]) {
                var tmp = weatherRepository2.getWeather(weatherRepository2_key, location_input).execute()
                    .body()?.results
                humidity.postValue((tmp?.humidity)?.toInt())
                weatherMaxTemp.postValue((tmp?.forecast?.get(0)?.max)?.toDouble())
                weatherMinTemp.postValue((tmp?.forecast?.get(0)?.min)?.toDouble())
            }
            else if (source==source_list[2]){

                // TODO: Add successful listener
                updateSourceRes(location_input)
                delay(100)

                if (sourceRes1.value != null && sourceRes2.value != null) {
                    var (a, b, c) = updateMix()
                    humidity.postValue(c)
                    weatherMaxTemp.postValue(("%.2f".format(a).toDouble()))
                    weatherMinTemp.postValue(("%.2f".format(b).toDouble()))
                }
            }
        }
    }

    suspend fun updateSourceRes(location_input: String) {
        var res1 = weatherRepository.getWeather(
            weatherRepository_key,
            "metric",
            location_input
        ).execute().body()?.main
        var res2 = weatherRepository2.getWeather(weatherRepository2_key, location_input).execute()
            .body()?.results
        sourceRes1.postValue(res1)
        sourceRes2.postValue(res2)
    }

    fun observeMaxTemp(): LiveData<Double> {
        return weatherMaxTemp
    }
    fun updateInfo(maxT: Double, minT: Double, Hum: Int){
        weatherMaxTemp.value = ("%.2f".format(maxT).toDouble())
        weatherMinTemp.value = ("%.2f".format(minT).toDouble())
        humidity.value = Hum
    }
    fun observeMinTemp(): LiveData<Double> {
        return weatherMinTemp
    }
    fun observeHumidity(): LiveData<Int> {
        return humidity
    }
    fun observeLocation(): LiveData<String>{
        return location
    }
    fun isFav(key: String): Boolean {
        return favlist.value?.contains(key) ?: false
    }
    fun addtoFav(location: String) {
        val localfavList = favlist.value?.toMutableList()

        localfavList?.let {
            it.add(location)
            favlist.value = it
        }
    }
    fun removefromFav(location: String) {
        val localfavList = favlist.value?.toMutableList()

        localfavList?.let {
            it.remove(location)
            favlist.value = it
        }
    }
    fun observeFav(): LiveData<List<String>>{
        return favlist
    }
    fun observeSW(): LiveData<List<Int>>{
        return source_weight
    }
    fun updateSW(idx: Int, value: Int) {
        val local_source_weight = source_weight.value?.toMutableList()
        local_source_weight?.set(idx, value)
        source_weight.value = local_source_weight
    }
    fun updateTheme(value: String){
        user_theme.value = value

    }
    fun observeTheme(): LiveData<String>{
        return user_theme
    }
    fun observeSource(): LiveData<String>{
        return sourceSelect
    }
    fun updateSource(value: String){
        sourceSelect.value = value
    }

    fun updateDocandSignout(){
        var data = mutableMapOf("sourcew" to source_weight.value,
                                "theme" to user_theme.value,
                                "favorite" to favlist.value)
        if (username != null) {
            var exist = false
            (db.collection("items").document(username)).get().addOnCompleteListener {
                if (it.result?.exists()!!){ exist =true}
            }
            if (exist) {
                db.collection("preference").document(username).update(
                    data
                ).addOnSuccessListener {
                    signOut()
                }
            }
            else{
                db.collection("preference").document(username).set(
                    data
                ).addOnSuccessListener {
                    signOut()
                }
            }
        }
    }
    fun updateMix(): Triple<Double?, Double?, Int?> {
        var w1 = source_weight.value?.get(0)
        var w2 = source_weight.value?.get(1)
        var mix_max: Double? = 0.0
        var mix_min: Double? = 0.0
        var mix_humidity: Int? = 0
        if (w1 != null) {
            mix_max=
                w2?.let {
                    (sourceRes2.value?.forecast?.get(0)?.max!!).times(
                        it
                    )
                }?.let { (sourceRes1.value?.temp_max)?.times(w1)?.plus(it) }
            if (mix_max != null) {
                mix_max = w2?.let { w1.plus(it) }?.let { mix_max!!.div(it) }
            }
            mix_min=
                w2?.let {
                    (sourceRes2.value?.forecast?.get(0)?.min!!).times(
                        it
                    )
                }?.let { (sourceRes1.value?.temp_min)?.times(w1)?.plus(it) }
            if (mix_min != null) {
                mix_min = w2?.let { w1.plus(it) }?.let { mix_min!!.div(it) }
            }
            mix_humidity=
                w2?.let {
                    (sourceRes2.value?.humidity?.toInt())?.times(
                        it
                    )
                }?.let { (sourceRes1.value?.humidity)?.times(w1)?.plus(it) }
            if (mix_humidity != null) {
                mix_humidity = w2?.let { w1.plus(it) }?.let { mix_humidity!!.div(it) }
            }
        }

        return Triple(mix_max, mix_min, mix_humidity)
    }
    fun setThemeColor(view: View, theme: String){
        when (theme){
            "beach" -> view.background = ResourcesCompat.getDrawable(view.resources, R.drawable.beach, null)
            "mountain" -> view.background = ResourcesCompat.getDrawable(view.resources, R.drawable.mountain, null)
            "rain" -> view.background = ResourcesCompat.getDrawable(view.resources, R.drawable.rain, null)
        }

    }
}
