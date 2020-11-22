package edu.cs371m.weather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.cs371m.weather.ui.main.MainFragment
import edu.cs371m.weather.ui.main.Favorites
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main_fragment.*

// https://opentdb.com/api_config.php
class MainActivity :
    AppCompatActivity()
{
    companion object {
        val TAG = this::class.java.simpleName
    }
    private val frags = listOf(
        MainFragment.newInstance() ,
        Favorites.newInstance()

    )
    private val viewModel: MainViewModel by viewModels() // XXX need to initialize the viewmodel (from an activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = "Weather Ensemble"
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            // XXX Write me: add fragments to layout, swipeRefresh

            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment, frags[0])
                .commitNow()

            // Please enjoy this code that manages the spinner
            // Create an ArrayAdapter using a simple spinner layout and languages array
        }
//        fav_but.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .show(frags[1])
//                .hide(frags[0])
//                .commitNow()
//        }
//        weather_but.setOnClickListener {
//            supportFragmentManager.beginTransaction()
//                .show(frags[0])
//                .hide(frags[1])
//                .commitNow()
//        }
    }
}
