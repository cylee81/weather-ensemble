package edu.cs371m.weather.api

import retrofit2.http.Query
import retrofit2.Call

class Repository(private val api: WeatherApi) {
    // XXX Write me.
    suspend fun getWeather(id: String, appId: String, units: String, location: String): Call<WeatherResponse> {
        return api.getWeather(id, appId, units, location)
    }
}

class Repository2(private val api: WeatherApi2) {
    // XXX Write me.
    suspend fun getWeather(key: String, city_name: String): Call<WeatherResponse2> {
        return api.getWeather(key, city_name)
    }
}