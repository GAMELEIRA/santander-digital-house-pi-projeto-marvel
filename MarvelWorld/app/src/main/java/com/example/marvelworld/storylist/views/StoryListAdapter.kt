package com.example.marvelworld.storylist.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.api.utils.NetworkUtils
import com.example.marvelworld.storylist.models.Story
import com.squareup.picasso.Picasso

class StoryListAdapter(
    private val storyList: List<Story>
) : RecyclerView.Adapter<StoryListAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.story_list_item, parent, false)

        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(story)
    }

    override fun getItemCount() = storyList.size

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_story_list_item)
        private val title: TextView = view.findViewById(R.id.title_story_list_item)

        @SuppressLint("SetTextI18n")
        fun bind(story: Story) {
            val path = story.thumbnail?.getImagePath() ?: NetworkUtils.NOT_FOUND_IMAGE
             Picasso.get().load(path).into(image)
            title.text = story.title
        }
    }
}