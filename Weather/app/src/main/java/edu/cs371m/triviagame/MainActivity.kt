package edu.cs371m.triviagame

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import edu.cs371m.triviagame.ui.main.MainFragment
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
        MainFragment.newInstance(0),
        MainFragment.newInstance(1),
        MainFragment.newInstance(2)
    )
    val difficultyList = listOf("Easy", "Medium", "Hard")
    private val viewModel: MainViewModel by viewModels() // XXX need to initialize the viewmodel (from an activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.title = "Trivia Game"
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            // XXX Write me: add fragments to layout, swipeRefresh

            supportFragmentManager.beginTransaction()
                .replace(R.id.q1, frags[0])
                .replace(R.id.q2, frags[1])
                .replace(R.id.q3, frags[2])
                .commitNow()

            swipeRefresh.setOnRefreshListener {
                viewModel.netRefresh()
                supportFragmentManager.beginTransaction()
                    .remove(frags[0])
                    .remove(frags[1])
                    .remove(frags[2])
                    .commitNow()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.q1, MainFragment.newInstance(0))
                    .replace(R.id.q2, MainFragment.newInstance(1))
                    .replace(R.id.q3, MainFragment.newInstance(2))
                    .commitNow()
                swipeRefresh.isRefreshing=false
            }


            // Please enjoy this code that manages the spinner
            // Create an ArrayAdapter using a simple spinner layout and languages array
            val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultyList)
            // Set layout to use when the list of choices appear
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            difficultySP.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    Log.d(TAG, "pos $position")
                    viewModel.setDifficulty(difficultyList[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    Log.d(TAG, "onNothingSelected")
                }
            }
            // Set Adapter to Spinner
            difficultySP.adapter = aa
            // Set initial value of spinner to medium
            val initialSpinner = 1
            difficultySP.setSelection(initialSpinner)
            viewModel.setDifficulty(difficultyList[initialSpinner])
        }
    }
}
