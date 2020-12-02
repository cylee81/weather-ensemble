package edu.cs371m.weather

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import edu.cs371m.weather.ui.main.MainFragment
import edu.cs371m.weather.ui.main.Favorites
import edu.cs371m.weather.ui.main.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main_fragment.*
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


// https://opentdb.com/api_config.php
class MainActivity :
    AppCompatActivity()
{
    companion object {
        val TAG = this::class.java.simpleName
    }
    private val frags = listOf(
        MainFragment.newInstance() ,
        Favorites.newInstance(),
        SettingsFragment.newInstance()
    )

    private val viewModel: MainViewModel by viewModels() // XXX need to initialize the viewmodel (from an activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = "Weather Ensemble"
        setSupportActionBar(toolbar)

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build())

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            1)

        if (savedInstanceState == null) {
            // XXX Write me: add fragments to layout, swipeRefresh

            supportFragmentManager.beginTransaction()
                .add(R.id.main_fragment, frags[1])
                .hide(frags[1])
                .add(R.id.main_fragment, frags[2])
                .hide(frags[2])
                .add(R.id.main_fragment, frags[0])
                .commitNow()

            // Please enjoy this code that manages the spinner
            // Create an ArrayAdapter using a simple spinner layout and languages array
        }
        fav_but.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .show(frags[1])
                .hide(frags[0])
                .hide(frags[2])
                .commitNow()
        }
        weather_but.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .show(frags[0])
                .hide(frags[1])
                .hide(frags[2])
                .commitNow()
        }
        setting_but.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .show(frags[2])
                .hide(frags[1])
                .hide(frags[0])
                .commitNow()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // RC_SIGN_IN is the request code you passed when starting the sign in flow.
        if (requestCode == 1) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == 1) {
                finish()
            }
        }
    }
}
