package edu.cs371m.weather.ui.main

import android.content.res.Resources
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.cs371m.weather.R
import edu.cs371m.weather.MainViewModel
import kotlinx.android.synthetic.main.row_post.view.*
import org.w3c.dom.Comment

/**
 * Created by witchel on 8/25/2019
 */

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
        val iconView = itemView.findViewById<ImageView>(R.id.star)
        // XXX Write me.
        // NB: This one-liner will exit the current fragment
        // (itemView.context as FragmentActivity).supportFragmentManager.popBackStack()

        fun bind(item: String) {
            // from https://github.com/utap-f2020/f2020-demo/blob/10dba900682082b89f629dddc985d23a26472322/FilterList/app/src/main/java/edu/cs371m/filterlist/ui/main/QuoteAdapter.kt

            textView.text = item
//            textView.setOnClickListener{
//                viewModel.setSubreddit(textView.text.toString())
//                viewModel.setTitleToSubreddit()
//                fragmentManager.popBackStack("home",1)
//            }
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