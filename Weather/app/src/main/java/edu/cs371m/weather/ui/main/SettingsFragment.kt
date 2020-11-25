package edu.cs371m.weather.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import edu.cs371m.weather.MainViewModel
import edu.cs371m.weather.R
import kotlinx.android.synthetic.main.setting_fragment.*

class SettingsFragment : Fragment(R.layout.setting_fragment)   {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
    private val viewModel: MainViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        s1num.text = viewModel.source_weight["s1"].toString()

        s2num.text = viewModel.source_weight["s2"].toString()

        s1m.setOnClickListener {
            var curr_num = ((s1num.text).toString()).toInt()
            if (curr_num > 0){
                curr_num -= 1
                viewModel.source_weight["s1"] = curr_num
                s1num.text = viewModel.source_weight["s1"].toString()
            }
        }
        s2m.setOnClickListener {
            var curr_num = ((s2num.text).toString()).toInt()
            if (curr_num > 0){
                curr_num -= 1
                viewModel.source_weight["s2"] = curr_num
                s2num.text = viewModel.source_weight["s2"].toString()
            }
        }
        s1p.setOnClickListener {
            var curr_num = ((s1num.text).toString()).toInt()
            curr_num += 1
            viewModel.source_weight["s2"] = curr_num
            s1num.text = viewModel.source_weight["s1"].toString()
        }
        s2p.setOnClickListener {
            var curr_num = ((s1num.text).toString()).toInt()
            curr_num += 1
            viewModel.source_weight["s2"] = curr_num
            s2num.text = viewModel.source_weight["s2"].toString()
        }
    }
}