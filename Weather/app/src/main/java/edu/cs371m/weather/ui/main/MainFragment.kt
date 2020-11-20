package edu.cs371m.weather.ui.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import edu.cs371m.weather.MainViewModel
import edu.cs371m.weather.R
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
        viewModel.observeTrivia().observe(viewLifecycleOwner, Observer {
            requireActivity()
            highT.text = it.temp_max.toString()
            lowT.text = it.temp_min.toString()
            rain.text = it.humidity.toString()
        })

    }
}
