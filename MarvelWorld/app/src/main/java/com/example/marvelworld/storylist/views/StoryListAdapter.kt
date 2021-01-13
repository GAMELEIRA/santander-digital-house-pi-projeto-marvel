package com.example.marvelworld.storylist.views

import android.content.Context
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.storylist.models.Story
import com.google.android.material.card.MaterialCardView

class StoryListAdapter(
    private val storyList: List<Story>,
    private val onStoryClickListener: OnStoryClickListener
) : RecyclerView.Adapter<StoryListAdapter.StoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.story_list_item, parent, false)

        return StoryViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = storyList[position]
        holder.bind(story, onStoryClickListener)
    }

    override fun getItemCount() = storyList.size

    class StoryViewHolder(
        view: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(view) {
        private val favoriteButton: ImageButton = view.findViewById(R.id.story_list_favorite_button)
        private val title: TextView = view.findViewById(R.id.story_list_item_title)
        private val cardView: MaterialCardView = view.findViewById(R.id.card)

        fun bind(story: Story, onStoryClickListener: OnStoryClickListener) {
            title.text = story.title

            if (story.isFavorite) {
                favoriteButton.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_favorite_button_set
                )

                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            } else {
                favoriteButton.background = ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_favorite_button_unset
                )

                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.dark_grey
                    )
                )
            }

            favoriteButton.setOnClickListener {
                onStoryClickListener.onStoryFavoriteClick(adapterPosition)
            }

            val gestureDetector =
                GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        onStoryClickListener.onStoryClick(adapterPosition)
                        return super.onSingleTapConfirmed(e)
                    }

                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        onStoryClickListener.onStoryFavoriteClick(adapterPosition)
                        return super.onDoubleTap(e)
                    }
                })

            itemView.setOnTouchListener { v, event ->
                gestureDetector.onTouchEvent(event)
                v.performClick()
                true
            }
        }
    }
}