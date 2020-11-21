package com.example.marvelworld.reusablecomponents.horizontallist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.google.android.material.chip.Chip

class HorizontalListAdapter(
    private val items: List<HorizontalListItem>,
    private val onHorizontalListItemClickListener: OnHorizontalListItemClickListener
) :
    RecyclerView.Adapter<HorizontalListAdapter.HorizontalItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalItemViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.horizontal_list_item, parent, false)

        return HorizontalItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorizontalItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onHorizontalListItemClickListener)
    }

    override fun getItemCount() = items.size

    class HorizontalItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: Chip = view.findViewById(R.id.horizontal_list_item)

        fun bind(
            item: HorizontalListItem,
            onHorizontalListItemClickListener: OnHorizontalListItemClickListener
        ) {
            title.text = item.name
            itemView.setOnClickListener {
                onHorizontalListItemClickListener.onHorizontalListItemClick(adapterPosition)
            }
        }
    }
}