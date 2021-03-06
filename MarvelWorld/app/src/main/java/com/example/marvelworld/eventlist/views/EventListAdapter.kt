package com.example.marvelworld.eventlist.views

import android.content.Context
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.util.Constant
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class EventListAdapter(
    private val eventList: MutableList<Event?>,
    private val onEventClickListener: OnEventClickListener
) : RecyclerView.Adapter<EventListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.event_list_item, parent, false)
            EventViewHolder(view, parent.context)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_progress_bar, parent, false)
            ProgressBarViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if (holder is EventViewHolder) {
            val event = eventList[position]!!
            holder.bind(event, onEventClickListener)
        }
    }

    override fun getItemCount() = eventList.size

    override fun getItemViewType(position: Int): Int {
        return if (eventList[position] != null) {
            Constant.VIEW_TYPE_ITEM
        } else {
            Constant.VIEW_TYPE_LOADING
        }
    }

    fun addNullData() {
        eventList.add(null)
        notifyDataSetChanged()
    }

    fun removeNull() {
        eventList.removeAt(eventList.size - 1)
        notifyDataSetChanged()
    }

    class EventViewHolder(
        view: View,
        private val context: Context
    ) : CustomViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_event_list_item)
        private val favoriteButton: ImageButton = view.findViewById(R.id.event_list_favorite_button)
        private val title: TextView = view.findViewById(R.id.title_event_list_item)
        private val cardView: MaterialCardView = view.findViewById(R.id.card)

        fun bind(eventModel: Event, onEventClickListener: OnEventClickListener) {
            val path = eventModel.thumbnail.getImagePath(Image.PORTRAIT_UNCANNY)
            Picasso.get().load(path).into(image)
            title.text = eventModel.title

            if (eventModel.isFavorite) {
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
                onEventClickListener.onEventFavoriteClick(adapterPosition)
            }

            val gestureDetector =
                GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        onEventClickListener.onEventClick(adapterPosition)
                        return super.onSingleTapConfirmed(e)
                    }

                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        onEventClickListener.onEventFavoriteClick(adapterPosition)
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