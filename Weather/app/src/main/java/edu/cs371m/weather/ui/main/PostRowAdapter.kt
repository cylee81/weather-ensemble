package edu.cs371m.weather.ui.main

import android.content.res.Resources
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import edu.cs371m.weather.MainActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.weather.R
import edu.cs371m.weather.MainViewModel
import kotlinx.android.synthetic.main.row_post.view.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

// This adapter inherits from ListAdapter, which should mean that all we need
// to do is give it a new list and an old list and as clients we will never
// have to call notifyDatasetChanged().  Well, unfortunately, I can't implement
// equal for SpannableStrings correctly.  So clients of this adapter are, under
// certain circumstances, going to have to call notifyDatasetChanged()
class CityListAdapter(private val viewModel: MainViewModel)
    : RecyclerView.Adapter<CityListAdapter.VH>() {

    private var posts = listOf<String>()
    // ViewHolder pattern minimizes calls to findViewById
    inner class VH(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        var fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
        val textView = itemView.findViewById<TextView>(R.id.city)
        val iconView = itemView.findViewById<ImageView>(R.id.trash)

        fun bind(item: String) {

            textView.text = item
            iconView.setOnClickListener {
                viewModel.removefromFav(item)
            }
            textView.setOnClickListener{
                fragmentManager.beginTransaction()
                    .show(MainActivity.frags[0])
                    .hide(MainActivity.frags[1])
                    .hide(MainActivity.frags[2])
                    .commitNow()
                viewModel.setLocation(item)
                
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_post, parent, false)
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(posts[holder.adapterPosition])
    }
    fun submitList(items: List<String>) {
        posts = items
    }

    override fun getItemCount(): Int {
        return posts.size
    }

}