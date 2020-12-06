package edu.cs371m.weather.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


interface WeatherApi {
    // This function needs to be called from a coroutine, hence the suspend
    // in its type.  Also note the @Query annotation, which says that when
    // called, retrofit will add "&difficulty=%s".format(level) to the URL
    // Thanks, retrofit!
    // Hardcode several parameters in the GET for simplicity
    // So URL can have & and ? characters
    // XXX Write me: Retrofit annotation, see CatNet
    @GET("/data/2.5/weather")
    fun getWeather(@Query("appid") appId: String,
                   @Query("units") unit: String, @Query("q") location: String): Call<WeatherResponse>

    // I just looked at the response and "parsed" it by eye

    data class Clouds(
        val all: Int? = null
    )
    data class Coord(
        val lon: Double? = null,
        val lat: Double? = null
    )
    data class Main(
        val temp: Double? = null,
        val temp_min: Double? = null,
        val humidity: Int? = null,
        val pressure: Int? = null,
        val temp_max: Double? = null
    )
    data class Sys(
        val country: String? = null,
        val sunrise: Int? = null,
        val sunset: Int? = null,
        val id: Int? = null,
        val type: Int? = null,
        val message: Double? = null
    )
    data class WeatherItem(
        val icon: String? = null,
        val description: String? = null,
        val main: String? = null,
        val id: Int? = null
    )
    data class Wind(
        val deg: Int? = null,
        val speed: Double? = null
    )

    companion object {
        // Leave this as a simple, base URL.  That way, we can have many different
        // functions (above) that access different "paths" on this server
        // https://square.github.io/okhttp/4.x/okhttp/okhttp3/-http-url/
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("api.openweathermap.org")
            .build()

        // Public create function that ties together building the base
        // URL and the private create function that initializes Retrofit
        fun create(): WeatherApi = create(url)
        private fun create(httpUrl: HttpUrl): WeatherApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
        }
    }
}

interface WeatherApi2 {
    @GET("/weather")
    fun getWeather(@Query("key") key: String, @Query("city_name") city_name: String): Call<WeatherResponse2>

    data class subRes (
        var temp: Int = 0,
        var date: String = "",
        var time: String = "",
        var condition_code: String = "",
        var description: String = "",
        var currently: String = "",
        var cid: String  = "",
        var city: String  = "",
        var img_id: String = "",
        var humidity: String = "",
        var wind_speedy: String = "",
        var sunrise: String = "",
        var sunset: String = "",
        var condition_slug: String = "",
        var city_name: String = "",
        var forecast: List<Forecast> = Collections.emptyList()
    )
    data class Forecast (
        var date: String = "",
        var weekday: String = "",
        var max: Int = 0,
        var min: Int = 0,
        var description: String = "",
        var condition: String  = ""
    )
//    data class WeatherResponse2(
//        val results: subRes? = null,
//        val valid_key: Boolean = true,
//        val by: String = ""
//    )

    companion object{
    var url = HttpUrl.Builder()
        .scheme("https")
        .host("api.hgbrasil.com")
        .build()

    fun create(): WeatherApi2 = create(url)

    private fun create(httpUrl: HttpUrl): WeatherApi2 {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi2::class.java)
        }
    }
}