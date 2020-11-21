package edu.cs371m.weather.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import edu.cs371m.weather.MainViewModel
import edu.cs371m.weather.R

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
        val adapter = PostRowAdapter(viewModel)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        viewModel.observeFavorite().observe(viewLifecycleOwner,
            Observer { postList ->
                //SSS
                if (postList != null){ Log.d("here", postList.size.toString())}
                adapter.submitList(postList)
                adapter.notifyDataSetChanged()
                //EEE // XXX Observer
            })
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rv, container, false)
        initAdapter(view)
//        val homeFragment = HomeFragment.newInstance()
//        val favview = activity?.findViewById<ImageView>(R.id.actionFavorite);
        var swipe = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)
        var fragmentManager = (view.context as FragmentActivity).supportFragmentManager
        swipe.setRefreshing(false)
        swipe.setEnabled(false)

        activity?.onBackPressedDispatcher?.addCallback(this) {
            viewModel.setFavFilt(false)
            Log.d("here", "favoriate makes setFavFilt False")
            viewModel.setTitleToSubreddit()
            fragmentManager.popBackStack("home",1)
        }
        return view
    }
    // If you want to get control when the user hits the system back button, get
    // a reference to the activity and call .onBackPressedDispatcher.addCallback(this){}
}