package com.example.marvelworld.creatorlist.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.creatorlist.models.Creator
import com.squareup.picasso.Picasso

class CreatorListAdapter(
    private val creatorList: List<Creator>,
    private val onCreatorClickListener: OnCreatorClickListener
) : RecyclerView.Adapter<CreatorListAdapter.CreatorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.creator_list_item, parent, false)

        return CreatorViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreatorViewHolder, position: Int) {
        val comic = creatorList[position]
        holder.bind(comic, onCreatorClickListener)
    }

    override fun getItemCount() = creatorList.size

    class CreatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_creator_list_item)
        private val title: TextView = view.findViewById(R.id.title_creator_list_item)

        @SuppressLint("SetTextI18n")
        fun bind(creator: Creator, onCreatorClickListener: OnCreatorClickListener) {
            val path = creator.thumbnail.getImagePath(Image.PORTRAIT_UNCANNY)
            Picasso.get().load(path).into(image)
            title.text = creator.fullName

            itemView.setOnClickListener {
                onCreatorClickListener.onCreatorClick(adapterPosition)
            }
        }
    }
}