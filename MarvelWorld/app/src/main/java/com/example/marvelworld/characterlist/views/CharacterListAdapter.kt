package com.example.marvelworld.characterlist.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.characterlist.models.Character
import com.squareup.picasso.Picasso

class CharacterListAdapter(
    private val characterList: List<Character>,
    private val onCharacterClickListener: OnCharacterClickListener
) : RecyclerView.Adapter<CharacterListAdapter.CharacterViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.character_list_item, parent, false)

        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characterList[position]
        holder.bind(character, onCharacterClickListener)
    }

    override fun getItemCount() = characterList.size

    class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.img_character_list_item)
        private val title: TextView = view.findViewById(R.id.title_character_list_item)

        fun bind(character: Character, onCharacterClickListener: OnCharacterClickListener) {
            val path = character.thumbnail.getImagePath()
            Picasso.get().load(path).into(image)
            title.text = character.name

            itemView.setOnClickListener {
                onCharacterClickListener.onCharacterClick(adapterPosition)
            }
        }
    }
}