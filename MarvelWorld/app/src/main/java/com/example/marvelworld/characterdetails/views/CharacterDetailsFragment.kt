package com.example.marvelworld.characterdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.characterdetails.repository.CharacterDetailsRepository
import com.example.marvelworld.characterdetails.viewmodel.CharacterDetailsViewModel
import com.example.marvelworld.reusablecomponents.expandablecard.Card
import com.example.marvelworld.reusablecomponents.expandablecard.ExpandableCardUtils
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener


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
            .observe(viewLifecycleOwner, { character ->
                val card = Card(
                    character.name,
                    character.thumbnail.getImagePath(Image.LANDSCAPE_INCREDIBLE),
                    character.thumbnail.getImagePath(Image.FULL_SIZE),
                    character.description,
                    character.urls
                )
                ExpandableCardUtils.initCard(view, card, childFragmentManager)
            })

        characterDetailsViewModel.getCharacterComics(characterId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Comics:",
                    this
                )
            })

        characterDetailsViewModel.getCharacterEvents(characterId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Events:",
                    this
                )
            })

        characterDetailsViewModel.getCharacterSeries(characterId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.series_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Series:",
                    this
                )
            })

        characterDetailsViewModel.getCharacterStories(characterId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Stories:",
                    this
                )
            })
    }

    override fun onHorizontalListItemClick(item: HorizontalListItem) {
        val bundle = bundleOf()
        when (item.type) {
            HorizontalListUtils.COMIC -> {
                bundle.putInt("COMIC_ID", item.id)
                findNavController().navigate(R.id.comicDetailsFragment, bundle)
            }
            HorizontalListUtils.EVENT -> {
                bundle.putInt("EVENT_ID", item.id)
                findNavController().navigate(R.id.eventDetailsFragment, bundle)
            }
            HorizontalListUtils.SERIES -> {
                bundle.putInt("SERIES_ID", item.id)
                findNavController().navigate(R.id.seriesDetailsFragment, bundle)
            }
            HorizontalListUtils.STORY -> {
                bundle.putInt("STORY_ID", item.id)
                findNavController().navigate(R.id.storyDetailsFragment, bundle)
            }
        }
    }
}