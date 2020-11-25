package edu.cs371m.weather

import android.R.attr.data
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import edu.cs371m.weather.api.Repository
import edu.cs371m.weather.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    // XXX You need some important member variables
    private val waetherapi = WeatherApi.create()
    private val weatherRepository = Repository(waetherapi)
    private val weatherAnswer = MutableLiveData<WeatherApi.Main>()
    private var location = MutableLiveData<String>()
    private var favlist = MutableLiveData<List<String>>().apply {
        value = mutableListOf()
    }
    var source_weight = mutableMapOf("s1" to 1, "s2" to 1)
    var user_theme = "theme1"

    init {
        // XXX one-liner to kick off the app
        if (location.value == null) {
            netRefresh("tokyo")
        } else {
            netRefresh((location.value).toString())
        }
        Log.d("location", "init")
    }
    fun setLocation(location_input: String) {
        location.value = location_input
        Log.d("location", location.value)
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
