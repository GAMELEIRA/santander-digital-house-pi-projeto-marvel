package com.example.marvelworld.characterdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.marvelworld.R
import com.example.marvelworld.characterdetails.repository.CharacterDetailsRepository
import com.example.marvelworld.characterdetails.viewmodel.CharacterDetailsViewModel
import com.example.marvelworld.reusablecomponents.ExpandableCardUtils
import com.example.marvelworld.reusablecomponents.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.OnHorizontalListItemClickListener


class CharacterDetailsFragment : Fragment(), OnHorizontalListItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val characterId = requireArguments().getInt("CHARACTER_ID")

        val characterDetailsViewModel = ViewModelProvider(
            this,
            CharacterDetailsViewModel.CharacterDetailsViewModelFactory(CharacterDetailsRepository())
        ).get(CharacterDetailsViewModel::class.java)

        characterDetailsViewModel.getCharacter(characterId)
            .observe(viewLifecycleOwner, Observer { character ->
                ExpandableCardUtils.initCard(view, character)
            })

        characterDetailsViewModel.getCharacterComics(characterId)
            .observe(viewLifecycleOwner, Observer {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Comics:",
                    this
                )
            })

        characterDetailsViewModel.getCharacterEvents(characterId)
            .observe(viewLifecycleOwner, Observer {
                val comicList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Events:",
                    this
                )
            })

        characterDetailsViewModel.getCharacterSeries(characterId)
            .observe(viewLifecycleOwner, Observer {
                val comicList = view.findViewById<LinearLayout>(R.id.series_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Series:",
                    this
                )
            })

        characterDetailsViewModel.getCharacterStories(characterId)
            .observe(viewLifecycleOwner, Observer {
                val comicList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Stories:",
                    this
                )
            })
    }

    override fun onHorizontalListItemClick(position: Int) {
        Toast.makeText(this.context, "cliquei", Toast.LENGTH_SHORT).show()
    }
}