package com.example.marvelworld.characterlist.views

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
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.util.Constant
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class CharacterListAdapter(
    private val characterList: MutableList<Character?>,
    private val onCharacterClickListener: OnCharacterClickListener,
) : RecyclerView.Adapter<CharacterListAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.character_list_item, parent, false)
            CharacterViewHolder(view, parent.context)
        } else {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recycler_progress_bar, parent, false)
            ProgressBarViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if (holder is CharacterViewHolder) {
            val character = characterList[position]!!
            holder.bind(character, onCharacterClickListener)
        }
    }

    override fun getItemCount() = characterList.size

    override fun getItemViewType(position: Int): Int {
        return if (characterList[position] != null) {
            Constant.VIEW_TYPE_ITEM
        } else {
            Constant.VIEW_TYPE_LOADING
        }
    }

    fun addNullData() {
        characterList.add(null)
        notifyDataSetChanged()
    }

    fun removeNull() {
        characterList.removeAt(characterList.size - 1)
        notifyDataSetChanged()
    }

    class CharacterViewHolder(
        view: View,
        private val context: Context
    ) : CustomViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_character_list_item)
        private val favoriteButton: ImageButton =
            view.findViewById(R.id.detail_card_favorite_button)
        private val title: TextView = view.findViewById(R.id.title_character_list_item)
        private val cardView: MaterialCardView = view.findViewById(R.id.card)

        fun bind(
            character: Character,
            onCharacterClickListener: OnCharacterClickListener
        ) {
            val path = character.thumbnail.getImagePath(Image.PORTRAIT_UNCANNY)
            Picasso.get().load(path).into(image)
            title.text = character.name

            if (character.isFavorite) {
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
                onCharacterClickListener.onCharacterFavoriteClick(adapterPosition)
            }

            val gestureDetector =
                GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                        onCharacterClickListener.onCharacterClick(adapterPosition)
                        return super.onSingleTapConfirmed(e)
                    }

                    override fun onDoubleTap(e: MotionEvent?): Boolean {
                        onCharacterClickListener.onCharacterFavoriteClick(adapterPosition)
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