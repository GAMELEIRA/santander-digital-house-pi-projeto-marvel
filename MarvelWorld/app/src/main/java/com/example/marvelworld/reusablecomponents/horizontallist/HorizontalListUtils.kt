package com.example.marvelworld.reusablecomponents.horizontallist

import android.graphics.Rect
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R

object HorizontalListUtils {
    const val CHARACTER = 0
    const val COMIC = 1
    const val EVENT = 2
    const val STORY = 3
    const val SERIES = 4
    const val CREATOR = 5

    fun initHorizontalList(
        view: View,
        list: List<HorizontalListItem>,
        title: String,
        onHorizontalListItemClickListener: OnHorizontalListItemClickListener
    ) {
        if (list.isNotEmpty()) {
            val titleList = view.findViewById<TextView>(R.id.horizontal_list_title)
            titleList.text = title

            val recycler = view.findViewById<RecyclerView>(R.id.horizontal_list)
            val manager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
            val itemsAdapter = HorizontalListAdapter(list, onHorizontalListItemClickListener)

            recycler.apply {
                adapter = itemsAdapter
                layoutManager = manager
                addItemDecoration(HorizontalListDecoration(list, 8))
            }
        } else {
            view.visibility = View.GONE
        }
    }

    private class HorizontalListDecoration(
        private val list: List<HorizontalListItem>,
        private val margin: Int
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildLayoutPosition(view)
            if (position < list.size - 1) outRect.right = margin
        }
    }
}