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
import com.example.marvelworld.detailcard.models.DetailCard
import com.example.marvelworld.detailcard.views.DetailCardFragment
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener
import com.example.marvelworld.util.ResourceType


class CharacterDetailsFragment :
    Fragment(),
    OnHorizontalListItemClickListener {

    private lateinit var characterDetailsViewModel: CharacterDetailsViewModel

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

        characterDetailsViewModel = ViewModelProvider(
            this,
            CharacterDetailsViewModel.CharacterDetailsViewModelFactory(
                CharacterDetailsRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(CharacterDetailsViewModel::class.java)

        characterDetailsViewModel.getCharacter(characterId)
            .observe(viewLifecycleOwner, { character ->
                val card = DetailCard(
                    character.id,
                    character.name,
                    character.thumbnail.getImagePath(Image.LANDSCAPE_INCREDIBLE),
                    character.thumbnail.getImagePath(Image.FULL_SIZE),
                    character.description,
                    character.urls,
                    ResourceType.CHARACTER,
                    character.isFavorite
                )

                childFragmentManager.beginTransaction().replace(
                    R.id.detail_card,
                    DetailCardFragment(card)
                ).commit()
            })

        characterDetailsViewModel.getCharacterComics(characterId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    getString(R.string.comics),
                    this
                )
            })

        characterDetailsViewModel.getCharacterEvents(characterId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    getString(R.string.events),
                    this
                )
            })

        characterDetailsViewModel.getCharacterSeries(characterId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.series_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    getString(R.string.series),
                    this
                )
            })

        characterDetailsViewModel.getCharacterStories(characterId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    getString(R.string.stories),
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