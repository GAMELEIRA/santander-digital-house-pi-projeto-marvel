package com.example.marvelworld.storylist.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.storylist.models.Story

class StoryListAdapter(
    private val storyList: List<Story>,
    private val onStoryClickListener: OnStoryClickListener
) : RecyclerView.Adapter<StoryListAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.story_list_item, parent, false)

        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(story, onStoryClickListener)
    }

    override fun getItemCount() = storyList.size

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.story_list_item_title)

        fun bind(story: Story, onStoryClickListener: OnStoryClickListener) {
            title.text = story.title

            itemView.setOnClickListener {
                onStoryClickListener.onStoryClick(adapterPosition)
            }
        }
    }
}