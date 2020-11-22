package edu.cs371m.weather.ui.main

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import edu.cs371m.weather.MainActivity
import edu.cs371m.weather.MainViewModel
import edu.cs371m.weather.R
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment :
    Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    private val viewModel:  MainViewModel by activityViewModels()
    // XXX initialize the viewModel

    private fun actionFavorite() {

        val favview = activity?.findViewById<TextView>(R.id.fav_but);
        val favFragment = Favorites.newInstance()

        favview?.setOnClickListener {
                parentFragmentManager?.beginTransaction()
                    ?.replace(R.id.main_fragment,  favFragment)
                    ?.addToBackStack("weather")
                    ?.commit()}

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val locationList = listOf("taipei", "tokyo", "seattle")
        val aa = ArrayAdapter(activity, android.R.layout.simple_spinner_item, locationList)
        // Set layout to use when the list of choices appear
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationSP.adapter = aa
        locationSP.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected( parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                Log.d("bug", "hi")
                viewModel.setLocation(locationList[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.d("bug", "here")
            }
        }

        star_but.setOnClickListener {
            var curr_location = location.text.toString()
            if(viewModel.isFav(curr_location)){
                star_but.setImageDrawable(ResourcesCompat.getDrawable(star_but.getContext().resources, R.drawable.unstar, null))
                viewModel.removefromFav(curr_location)
            }
            else{
                star_but.setImageDrawable(ResourcesCompat.getDrawable(star_but.getContext().resources, R.drawable.star, null))
                viewModel.addtoFav(curr_location)
            }
        }

        // Set Adapter to Spinner
        val initialSpinner = 1
        locationSP.setSelection(initialSpinner)
        viewModel.setLocation(locationList[initialSpinner])

        viewModel.observeWeather().observe(viewLifecycleOwner, Observer {
            requireActivity()
            highT.text = it.temp_max.toString()
            lowT.text = it.temp_min.toString()
            rain.text = it.humidity.toString()
        })
        viewModel.observeLocation().observe(viewLifecycleOwner, Observer {
            location.text = it
            viewModel.netRefresh(it)
            if(viewModel.isFav(it)){
                star_but.setImageDrawable(ResourcesCompat.getDrawable(star_but.getContext().resources, R.drawable.star, null))
            }
            else{
                star_but.setImageDrawable(ResourcesCompat.getDrawable(star_but.getContext().resources, R.drawable.unstar, null))
            }
        })
        actionFavorite()
    }
}
