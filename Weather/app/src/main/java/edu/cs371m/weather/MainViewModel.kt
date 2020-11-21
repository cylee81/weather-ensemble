package edu.cs371m.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cs371m.weather.api.Repository
import edu.cs371m.weather.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {
    // XXX You need some important member variables
    private var location = "taipei"
    private val waetherapi = WeatherApi.create()
    private val weatherRepository = Repository(waetherapi)
    private val weatherAnswer = MutableLiveData<WeatherApi.Main>()

    init {
        // XXX one-liner to kick off the app
        netRefresh()
    }
    fun setLocation(level: String) {
        location = location
        Log.d(javaClass.simpleName, "Location: $location")
    }


    fun netRefresh() {
        // XXX Write me.  This is where the network request is initiated.
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO) {
            // Update LiveData from IO dispatcher, use postValue
            val tmp = weatherRepository.getWeather("745044", "7dddf551a056642a1ae589fdb97aa5ec", "metric").execute().body()?.main
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

}
