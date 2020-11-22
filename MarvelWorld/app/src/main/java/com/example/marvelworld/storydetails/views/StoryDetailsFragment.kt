package com.example.marvelworld.storydetails.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marvelworld.R
import com.example.marvelworld.reusablecomponents.expandablecard.Card
import com.example.marvelworld.reusablecomponents.expandablecard.ExpandableCardUtils
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener
import com.example.marvelworld.storydetails.repository.StoryDetailsRepository
import com.example.marvelworld.storydetails.viewmodel.StoryDetailsViewModel

class StoryDetailsFragment : Fragment(), OnHorizontalListItemClickListener {

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

        val storyDetailsViewModel = ViewModelProvider(
            this,
            StoryDetailsViewModel.StoryDetailsViewModelFactory(StoryDetailsRepository())
        ).get(StoryDetailsViewModel::class.java)

        storyDetailsViewModel.getStory(storyId)
            .observe(viewLifecycleOwner, Observer { story ->
                val card = Card(
                    story.title,
                    story.thumbnail?.getImagePath(),
                    story.description,
                    null
                )
                ExpandableCardUtils.initCard(view, card, childFragmentManager)
            })

        storyDetailsViewModel.getStoryCharacters(storyId)
            .observe(viewLifecycleOwner, Observer {
                val characterList = view.findViewById<LinearLayout>(R.id.character_list)
                HorizontalListUtils.initHorizontalList(
                    characterList,
                    it,
                    "Characters:",
                    this
                )
            })

        storyDetailsViewModel.getStoryComics(storyId)
            .observe(viewLifecycleOwner, Observer {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Comics:",
                    this
                )
            })

        storyDetailsViewModel.getStoryEvents(storyId)
            .observe(viewLifecycleOwner, Observer {
                val eventList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    eventList,
                    it,
                    "Events:",
                    this
                )
            })

        storyDetailsViewModel.getStorySeries(storyId)
            .observe(viewLifecycleOwner, Observer {
                val seriesList = view.findViewById<LinearLayout>(R.id.series_list)
                HorizontalListUtils.initHorizontalList(
                    seriesList,
                    it,
                    "Series:",
                    this
                )
            })

        storyDetailsViewModel.getStoryCreators(storyId)
            .observe(viewLifecycleOwner, Observer {
                val creatorList = view.findViewById<LinearLayout>(R.id.creator_list)
                HorizontalListUtils.initHorizontalList(
                    creatorList,
                    it,
                    "Creators:",
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