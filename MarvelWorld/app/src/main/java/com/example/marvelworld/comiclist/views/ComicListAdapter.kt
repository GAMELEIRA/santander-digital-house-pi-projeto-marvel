package com.example.marvelworld.comiclist.views

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
import com.example.marvelworld.comiclist.models.Comic
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class ComicListAdapter(
    private val comicList: List<Comic>,
    private val onComicClickListener: OnComicClickListener
) : RecyclerView.Adapter<ComicListAdapter.ComicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.comic_list_item, parent, false)

        return ComicViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comic = comicList[position]
        holder.bind(comic, onComicClickListener)
    }

    override fun getItemCount() = comicList.size

    class ComicViewHolder(
        view: View,
        private val context: Context
    ) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_comic_list_item)
        private val favoriteButton: ImageButton = view.findViewById(R.id.comic_list_favorite_button)
        private val title: TextView = view.findViewById(R.id.title_comic_list_item)
        private val cardView: MaterialCardView = view.findViewById(R.id.card)

        fun bind(comic: Comic, onComicClickListener: OnComicClickListener) {
            val path = comic.thumbnail.getImagePath(Image.PORTRAIT_UNCANNY)
            Picasso.get().load(path).into(image)
            title.text = comic.title

            if (comic.isFavorite) {
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
                onComicClickListener.onComicFavoriteClick(adapterPosition)
            }

            val gestureDetector =
                GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        onComicClickListener.onComicClick(adapterPosition)
                        return super.onSingleTapConfirmed(e)
                    }

                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        onComicClickListener.onComicFavoriteClick(adapterPosition)
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