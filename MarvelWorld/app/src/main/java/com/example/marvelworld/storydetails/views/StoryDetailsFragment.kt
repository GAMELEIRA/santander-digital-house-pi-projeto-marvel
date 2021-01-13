package com.example.marvelworld.storydetails.views

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
import com.example.marvelworld.detailcard.models.DetailCard
import com.example.marvelworld.detailcard.views.DetailCardFragment
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener
import com.example.marvelworld.storydetails.repository.StoryDetailsRepository
import com.example.marvelworld.storydetails.viewmodel.StoryDetailsViewModel
import com.example.marvelworld.util.ResourceType

class StoryDetailsFragment :
    Fragment(),
    OnHorizontalListItemClickListener {

    private lateinit var storyDetailsViewModel: StoryDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storyId = requireArguments().getInt("STORY_ID")

        storyDetailsViewModel = ViewModelProvider(
            this,
            StoryDetailsViewModel.StoryDetailsViewModelFactory(
                StoryDetailsRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(StoryDetailsViewModel::class.java)

        storyDetailsViewModel.getStory(storyId)
            .observe(viewLifecycleOwner, { story ->
                val card = DetailCard(
                    story.id,
                    story.title,
                    story.thumbnail?.getImagePath(Image.LANDSCAPE_INCREDIBLE),
                    story.thumbnail?.getImagePath(Image.FULL_SIZE),
                    story.description,
                    null,
                    ResourceType.STORY,
                    story.isFavorite
                )

                childFragmentManager.beginTransaction().replace(
                    R.id.detail_card,
                    DetailCardFragment(card)
                ).commit()
            })

        storyDetailsViewModel.getStoryCharacters(storyId)
            .observe(viewLifecycleOwner, {
                val characterList = view.findViewById<LinearLayout>(R.id.character_list)
                HorizontalListUtils.initHorizontalList(
                    characterList,
                    it,
                    getString(R.string.characters),
                    this
                )
            })

        storyDetailsViewModel.getStoryComics(storyId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    getString(R.string.comics),
                    this
                )
            })

        storyDetailsViewModel.getStoryEvents(storyId)
            .observe(viewLifecycleOwner, {
                val eventList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    eventList,
                    it,
                    getString(R.string.events),
                    this
                )
            })

        storyDetailsViewModel.getStorySeries(storyId)
            .observe(viewLifecycleOwner, {
                val seriesList = view.findViewById<LinearLayout>(R.id.series_list)
                HorizontalListUtils.initHorizontalList(
                    seriesList,
                    it,
                    getString(R.string.series),
                    this
                )
            })

        storyDetailsViewModel.getStoryCreators(storyId)
            .observe(viewLifecycleOwner, {
                val creatorList = view.findViewById<LinearLayout>(R.id.creator_list)
                HorizontalListUtils.initHorizontalList(
                    creatorList,
                    it,
                    getString(R.string.creators),
                    this
                )
            })
    }

    override fun onHorizontalListItemClick(item: HorizontalListItem) {
        val bundle = bundleOf()
        when (item.type) {
            HorizontalListUtils.CHARACTER -> {
                bundle.putInt("CHARACTER_ID", item.id)
                findNavController().navigate(R.id.characterDetailsFragment, bundle)
            }
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
            HorizontalListUtils.CREATOR -> {
                bundle.putInt("CREATOR_ID", item.id)
                findNavController().navigate(R.id.creatorDetailsFragment, bundle)
            }
        }
    }
}