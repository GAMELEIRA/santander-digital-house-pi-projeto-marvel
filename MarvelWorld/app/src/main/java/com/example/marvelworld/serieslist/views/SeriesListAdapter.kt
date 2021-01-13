package com.example.marvelworld.serieslist.views

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
import com.example.marvelworld.serieslist.models.Series
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class SeriesListAdapter(
    private val seriesList: List<Series>,
    private val onSeriesClickListener: OnSeriesClickListener
) : RecyclerView.Adapter<SeriesListAdapter.SeriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.series_list_item, parent, false)

        return SeriesViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val series = seriesList[position]
        holder.bind(series, onSeriesClickListener)
    }

    override fun getItemCount() = seriesList.size

    class SeriesViewHolder(
        view: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_series_list_item)
        private val favoriteButton: ImageButton =
            view.findViewById(R.id.series_list_favorite_button)
        private val title: TextView = view.findViewById(R.id.title_series_list_item)
        private val cardView: MaterialCardView = view.findViewById(R.id.card)

        fun bind(series: Series, onSeriesClickListener: OnSeriesClickListener) {
            val path = series.thumbnail.getImagePath(Image.PORTRAIT_UNCANNY)
            Picasso.get().load(path).into(image)
            title.text = series.title

            if (series.isFavorite) {
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
                onSeriesClickListener.onSeriesFavoriteClick(adapterPosition)
            }

            val gestureDetector =
                GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        onSeriesClickListener.onSeriesClick(adapterPosition)
                        return super.onSingleTapConfirmed(e)
                    }

                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        onSeriesClickListener.onSeriesFavoriteClick(adapterPosition)
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