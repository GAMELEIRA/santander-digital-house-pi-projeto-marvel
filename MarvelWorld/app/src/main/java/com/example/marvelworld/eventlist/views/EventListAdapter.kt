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
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class EventListAdapter(
    private val eventList: List<Event>,
    private val onEventClickListener: OnEventClickListener
) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.event_list_item, parent, false)

        return EventViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event, onEventClickListener)
    }

    override fun getItemCount() = eventList.size

    class EventViewHolder(
        view: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(view) {
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
}