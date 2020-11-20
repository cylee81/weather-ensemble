package edu.cs371m.weather.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    // This function needs to be called from a coroutine, hence the suspend
    // in its type.  Also note the @Query annotation, which says that when
    // called, retrofit will add "&difficulty=%s".format(level) to the URL
    // Thanks, retrofit!
    // Hardcode several parameters in the GET for simplicity
    // So URL can have & and ? characters
    // XXX Write me: Retrofit annotation, see CatNet
    @GET("/data/2.5/weather")
    fun getWeather(@Query("id") id: String, @Query("appid") appId: String, @Query("units") unit: String): Call<WeatherResponse>

    // XXX Write me: The return type
    //  suspend fun getThree(@Query("difficulty") level: String) :TriviaResponse

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