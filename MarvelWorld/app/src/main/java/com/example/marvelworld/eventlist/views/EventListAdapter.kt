package com.example.marvelworld.eventlist.views

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.eventlist.models.Event
import com.squareup.picasso.Picasso

class EventListAdapter(
    private val eventList: List<Event>,
    private val onEventClickListener: OnEventClickListener
) : RecyclerView.Adapter<EventListAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.event_list_item, parent, false)

        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.bind(event, onEventClickListener)
    }

    override fun getItemCount() = eventList.size

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_event_list_item)
        private val title: TextView = view.findViewById(R.id.title_event_list_item)

        @SuppressLint("SetTextI18n")
        fun bind(event: Event, onEventClickListener: OnEventClickListener) {
            val path = event.thumbnail.getImagePath(Image.PORTRAIT_UNCANNY)
            Picasso.get().load(path).into(image)
            title.text = event.title

            itemView.setOnClickListener {
                onEventClickListener.onEventClick(adapterPosition)
            }
        }
    }
}