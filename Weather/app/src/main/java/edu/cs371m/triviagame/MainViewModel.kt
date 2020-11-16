package edu.cs371m.triviagame

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.cs371m.triviagame.api.Repository
import edu.cs371m.triviagame.api.TriviaApi
import edu.cs371m.triviagame.api.TriviaQuestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {
    private var difficulty = "badValue"
    // XXX You need some important member variables
    private val triviaapi = TriviaApi.create()
    private val triviaRepository = Repository(triviaapi)
    private val triviaAnswer = MutableLiveData<TriviaApi.TriviaResponse>()

    init {
        // XXX one-liner to kick off the app
        netRefresh()
    }

    fun setDifficulty(level: String) {
        difficulty = when(level.toLowerCase(Locale.getDefault())) {
            // Sanitize input
            "easy" -> "easy"
            "medium" -> "medium"
            "hard" -> "hard"
            else -> "medium"
        }
        Log.d(javaClass.simpleName, "level $level END difficulty $difficulty")
    }

    fun netRefresh() {
        // XXX Write me.  This is where the network request is initiated.
        setDifficulty(difficulty)
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO) {
            // Update LiveData from IO dispatcher, use postValue
            triviaAnswer.postValue(triviaRepository.getThree(difficulty))
        }
    }
    // XXX Another function is necessary
    fun observeTrivia(): LiveData<TriviaApi.TriviaResponse> {
        return triviaAnswer
    }
}
