package edu.cs371m.weather

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import edu.cs371m.weather.api.Repository
import edu.cs371m.weather.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.firebase.firestore.FirebaseFirestore


class MainViewModel : ViewModel() {
    // XXX You need some important member variables
    private val waetherapi = WeatherApi.create()
    private val weatherRepository = Repository(waetherapi)
    private val weatherAnswer = MutableLiveData<WeatherApi.Main>()
    private var location = MutableLiveData<String>()
    private var favlist = MutableLiveData<List<String>>().apply {
        value = mutableListOf()
    }
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val username = FirebaseAuth.getInstance().currentUser?.email

    var source_weight = mutableMapOf("s1" to 1, "s2" to 1)
    var user_theme = "theme1"

    init {
        // XXX one-liner to kick off the app
        if (location.value == null) {
            netRefresh("tokyo")
        } else {
            netRefresh((location.value).toString())
        }
        Log.d("db", username.toString())


        db.collection("preference")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == username){
                        Log.d("db", "${document.id} => ${document.data}")
                        source_weight = document.data["sourcew"] as MutableMap<String, Int>
                        Log.d("db", "${document.id} => ${source_weight}")
                        user_theme = document.data["theme"] as String
                        var fav_arr = document.data["favorite"] as ArrayList<*>
                        for (i in fav_arr.indices){
                            addtoFav(fav_arr[i].toString())
                            Log.d("db", "${fav_arr[i]}")
                        }
                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.d("db", "Error getting documents: ", exception)
            }

//        if (docRef != null) {
//            Log.d("db", docRef.result.toString())
//        }
    }
    fun setLocation(location_input: String) {
        location.value = location_input
        Log.d("location", location.value)
    }
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

    fun netRefresh(location_input: String) {
        // XXX Write me.  This is where the network request is initiated.
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO) {
            // Update LiveData from IO dispatcher, use postValue
            val tmp = weatherRepository.getWeather("745044",
                                                "7dddf551a056642a1ae589fdb97aa5ec",
                                                 "metric",
                                                        location_input).execute().body()?.main
            if (tmp != null) {
                Log.d("API", (tmp.temp).toString())
            }
            weatherAnswer.postValue(tmp)
        }
    }

    // XXX Another function is necessary
    fun observeWeather(): LiveData<WeatherApi.Main> {
        return weatherAnswer
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
        Log.d("fav", favlist.value?.get(0))
    }
    fun removefromFav(location: String) {
        val localfavList = favlist.value?.toMutableList()

        localfavList?.let {
            it.remove(location)
            favlist.value = it
        }
    }
    fun observeFav(): LiveData<List<String>>{
        Log.d("here", "observing")
        return favlist
    }

}
