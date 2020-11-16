package edu.cs371m.triviagame.api

class Repository(private val api: TriviaApi) {
    // XXX Write me.
    suspend fun getThree(level: String): TriviaApi.TriviaResponse {
        return api.getThree(level)
    }
}