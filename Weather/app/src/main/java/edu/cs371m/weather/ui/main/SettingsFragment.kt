package edu.cs371m.weather.ui.main

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import edu.cs371m.weather.MainActivity
import edu.cs371m.weather.MainViewModel
import edu.cs371m.weather.R
import kotlinx.android.synthetic.main.setting_fragment.*


class SettingsFragment : Fragment(R.layout.setting_fragment)   {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
    private val viewModel: MainViewModel by viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeSW().observe(viewLifecycleOwner,
            Observer { SWmap ->
                Log.d("SW", "fragment observing")
                s1num.text = SWmap[0].toString()
                s2num.text = SWmap[1].toString()
            }
        )
        viewModel.observeTheme().observe(viewLifecycleOwner,
            Observer { theme ->
                t1.setTypeface(null, Typeface.NORMAL);
                t2.setTypeface(null, Typeface.NORMAL);
                t3.setTypeface(null, Typeface.NORMAL);

                if (theme == "theme1"){
                    t1.setTypeface(null, Typeface.BOLD)
                }
                if (theme == "theme2"){
                    t2.setTypeface(null, Typeface.BOLD);
                }
                if (theme == "theme3"){
                    t3.setTypeface(null, Typeface.BOLD);
                }

            }

        )

        s1m.setOnClickListener {
            var curr_num = ((s1num.text).toString()).toInt()
            if (curr_num > 0){
                curr_num -= 1
                viewModel.updateSW(0, curr_num)
                Log.d("SW", "----")
            }
        }
        s2m.setOnClickListener {
            var curr_num = ((s2num.text).toString()).toInt()
            if (curr_num > 0){
                curr_num -= 1
                viewModel.updateSW(1,  curr_num)
            }
        }
        s1p.setOnClickListener {
            var curr_num = ((s1num.text).toString()).toInt()
            curr_num += 1
            viewModel.updateSW(0, curr_num)
            Log.d("SW", "++++")
        }
        s2p.setOnClickListener {
            var curr_num = ((s2num.text).toString()).toInt()
            curr_num += 1
            viewModel.updateSW(1, curr_num)
        }

        t1.setOnClickListener {
            viewModel.updateTheme("theme1")
        }
        t2.setOnClickListener {
            viewModel.updateTheme("theme2")
        }
        t3.setOnClickListener {
            viewModel.updateTheme("theme3")
        }

        logout.setOnClickListener {
            Log.d("signout", "i am signing out")
            viewModel.updateDocandSignout()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
    }
}