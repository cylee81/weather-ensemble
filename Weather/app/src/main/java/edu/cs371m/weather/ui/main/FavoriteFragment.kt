package edu.cs371m.weather.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.cs371m.weather.MainViewModel
import edu.cs371m.weather.R
import edu.cs371m.weather.ui.main.CityListAdapter
import kotlinx.android.synthetic.main.favorite.*
import kotlinx.android.synthetic.main.row_post.*

class Favorites: Fragment() {
    // XXX initialize viewModel
    private val viewModel: MainViewModel by activityViewModels()
    companion object {
        fun newInstance(): Favorites {
            return Favorites()
        }
    }
    private fun initAdapter(root: View) {
        val rv = root.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = CityListAdapter(viewModel)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        viewModel.observeFav().observe(viewLifecycleOwner,
            Observer { postList ->
                adapter.submitList(postList)
                adapter.notifyDataSetChanged()
            })
        viewModel.observeTheme().observe(viewLifecycleOwner,
            Observer { theme ->
                viewModel.setThemeColor(main, theme)
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.favorite, container, false)
        initAdapter(view)
        return view
    }
    // If you want to get control when the user hits the system back button, get
    // a reference to the activity and call .onBackPressedDispatcher.addCallback(this){}
}