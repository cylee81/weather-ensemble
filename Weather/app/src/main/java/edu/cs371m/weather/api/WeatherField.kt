package edu.cs371m.weather.api

import com.google.gson.annotations.SerializedName
import edu.cs371m.weather.api.WeatherApi.*

data class WeatherResponse(

    @SerializedName("dt")
    val dt: Int? = null,

    @SerializedName("coord")
    val coord: Coord? = null,

    @SerializedName("visibility")
    val visibility: Int? = null,

    @SerializedName("weather")
    val weather: List<WeatherItem?>? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("cod")
    val cod: Int? = null,

    @SerializedName("main")
    val main: Main? = null,

    @SerializedName("clouds")
    val clouds: Clouds? = null,

    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("sys")
    val sys: Sys? = null,

    @SerializedName("base")
    val base: String? = null,

    @SerializedName("wind")
    val wind: Wind? = null
)