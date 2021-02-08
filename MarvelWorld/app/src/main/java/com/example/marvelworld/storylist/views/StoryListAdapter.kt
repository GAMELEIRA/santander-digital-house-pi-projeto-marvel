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
import com.example.marvelworld.util.Constant
import com.google.android.material.card.MaterialCardView

class StoryListAdapter(
    private val storyList: MutableList<Story?>,
    private val onStoryClickListener: OnStoryClickListener
) : RecyclerView.Adapter<StoryListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.story_list_item, parent, false)
            StoryViewHolder(view, parent.context)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_progress_bar, parent, false)
            ProgressBarViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if(holder is StoryViewHolder) {
            val story = storyList[position]!!
            holder.bind(story, onStoryClickListener)
        }
    }

    override fun getItemCount() = storyList.size

    override fun getItemViewType(position: Int): Int {
        return if (storyList[position] != null) {
            Constant.VIEW_TYPE_ITEM
        } else {
            Constant.VIEW_TYPE_LOADING
        }
    }

    fun addNullData() {
        storyList.add(null)
        notifyDataSetChanged()
    }

    fun removeNull() {
        storyList.removeAt(storyList.size - 1)
        notifyDataSetChanged()
    }

    class StoryViewHolder(
        view: View,
        private val context: Context
    ) : CustomViewHolder(view) {
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

    class ProgressBarViewHolder(view: View) : CustomViewHolder(view)
    open class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)
}