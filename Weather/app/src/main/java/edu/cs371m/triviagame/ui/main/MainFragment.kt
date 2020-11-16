package edu.cs371m.triviagame.ui.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import edu.cs371m.triviagame.MainViewModel
import edu.cs371m.triviagame.R
import edu.cs371m.triviagame.api.TriviaQuestion
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment :
    Fragment(R.layout.main_fragment) {

    companion object {
        const val idKey = "idKey"
        fun newInstance(id: Int): MainFragment {
            val b = Bundle()
            b.putInt(idKey, id)
            val frag = MainFragment()
            frag.arguments = b
            return frag
        }
    }

    private val viewModel: MainViewModel by viewModels()
    // XXX initialize the viewModel

    private fun setClickListeners(question: TriviaQuestion, tv: TextView, tb: Button, fb: Button) {
        // XXX Write me Color.GREEN for correct, Color.RED for incorrect
        tb.setOnClickListener{ v ->
            if (question.correctAnswer == tb.text){
                tv.setBackgroundColor(Color.GREEN)
            }
            else{
                tv.setBackgroundColor(Color.RED)
            }
        }
        fb.setOnClickListener{ v ->
            if (question.correctAnswer == fb.text){
                tv.setBackgroundColor(Color.GREEN)
            }
            else{
                tv.setBackgroundColor(Color.RED)
            }
        }
    }
    // Corrects some ugly HTML encodings
    private fun fromHtml(source: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            @Suppress("DEPRECATION")
            return Html.fromHtml(source).toString()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // XXX Write me.  viewModel should observe something
        // When it gets what it is observing, it should index into it
        // You might find the requireArguments() function useful
        // You should "turn off" the swipe refresh spinner.  You might
        // find the requireActivity() function useful for this.
        resetQColor()
        viewModel.observeTrivia().observe(viewLifecycleOwner, Observer {
            requireActivity()
            var question = it.results[requireArguments().getInt(idKey)]
            Log.d("tag", question.question)
            qTV.text = fromHtml(question.question)
            setClickListeners(question, qTV, qTrueBut, qFalseBut)
        })

    }
    private fun resetQColor() {
        qTV.setBackgroundColor(Color.TRANSPARENT)
    }
}
