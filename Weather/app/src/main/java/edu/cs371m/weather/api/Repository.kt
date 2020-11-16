package edu.cs371m.weather.api

class Repository(private val api: TriviaApi) {
    // XXX Write me.
    suspend fun getThree(level: String): TriviaApi.TriviaResponse {
        return api.getThree(level)
    }
}