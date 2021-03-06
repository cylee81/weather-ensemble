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
import kotlinx.coroutines.delay


class SettingsFragment : Fragment(R.layout.setting_fragment)   {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }
    private val viewModel: MainViewModel by activityViewModels() //viewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.observeSW().observe(viewLifecycleOwner,
            Observer { SWmap ->
                s1num.text = SWmap[0].toString()
                s2num.text = SWmap[1].toString()
            }
        )
        viewModel.observeTheme().observe(viewLifecycleOwner,
            Observer { theme ->
                t1.setTypeface(null, Typeface.NORMAL);
                t2.setTypeface(null, Typeface.NORMAL);
                t3.setTypeface(null, Typeface.NORMAL);

                if (theme == "beach"){
                    t1.setTypeface(null, Typeface.BOLD)
                }
                if (theme == "mountain"){
                    t2.setTypeface(null, Typeface.BOLD);
                }
                if (theme == "rain"){
                    t3.setTypeface(null, Typeface.BOLD);
                }
                viewModel.setThemeColor(main, theme)

            }

        )

        s1m.setOnClickListener {
            var curr_num = ((s1num.text).toString()).toInt()
            if (curr_num > 0){
                curr_num -= 1
                viewModel.updateSW(0, curr_num)
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
        }
        s2p.setOnClickListener {
            var curr_num = ((s2num.text).toString()).toInt()
            curr_num += 1
            viewModel.updateSW(1, curr_num)
        }

        t1.setOnClickListener {
            viewModel.updateTheme("beach")
        }
        t2.setOnClickListener {
            viewModel.updateTheme("mountain")
        }
        t3.setOnClickListener {
            viewModel.updateTheme("rain")
        }

        logout.setOnClickListener {
            viewModel.updateDocandSignout()
            Thread.sleep(1_000)
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }
    }
}