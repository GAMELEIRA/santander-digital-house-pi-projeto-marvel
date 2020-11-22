package com.example.marvelworld.serieslist.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.serieslist.models.Series
import com.squareup.picasso.Picasso

class SeriesListAdapter(
    private val seriesList: List<Series>,
    private val onSeriesClickListener: OnSeriesClickListener
) : RecyclerView.Adapter<SeriesListAdapter.SeriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.series_list_item, parent, false)

        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val series = seriesList[position]
        holder.bind(series, onSeriesClickListener)
    }

    override fun getItemCount() = seriesList.size

    class SeriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_series_list_item)
        private val title: TextView = view.findViewById(R.id.title_series_list_item)

        fun bind(series: Series, onSeriesClickListener: OnSeriesClickListener) {
            val path = series.thumbnail.getImagePath()
            Picasso.get().load(path).into(image)
            title.text = series.title

            itemView.setOnClickListener {
                onSeriesClickListener.onSeriesClick(adapterPosition)
            }
        }
    }
}