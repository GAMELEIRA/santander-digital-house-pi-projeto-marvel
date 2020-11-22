package com.example.marvelworld.eventdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.eventdetails.respository.EventDetailsRepository
import com.example.marvelworld.eventdetails.viewmodel.EventDetailsViewModel
import com.example.marvelworld.reusablecomponents.expandablecard.Card
import com.example.marvelworld.reusablecomponents.expandablecard.ExpandableCardUtils
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener

class EventDetailsFragment : Fragment(), OnHorizontalListItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventId = requireArguments().getInt("EVENT_ID")

        val eventDetailsViewModel = ViewModelProvider(
            this,
            EventDetailsViewModel.EventDetailsViewModelFactory(EventDetailsRepository())
        ).get(EventDetailsViewModel::class.java)

        eventDetailsViewModel.getEvent(eventId)
            .observe(viewLifecycleOwner, Observer { event ->
                val card = Card(
                    event.title,
                    event.thumbnail.getImagePath(Image.DETAIL),
                    event.thumbnail.getImagePath(Image.FULL_SIZE),
                    event.description,
                    event.urls
                )
                ExpandableCardUtils.initCard(view, card, childFragmentManager)
            })

        eventDetailsViewModel.getEventCharacters(eventId)
            .observe(viewLifecycleOwner, Observer {
                val characterList = view.findViewById<LinearLayout>(R.id.character_list)
                HorizontalListUtils.initHorizontalList(
                    characterList,
                    it,
                    "Characters:",
                    this
                )
            })

        eventDetailsViewModel.getEventComics(eventId)
            .observe(viewLifecycleOwner, Observer {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Comics:",
                    this
                )
            })

        eventDetailsViewModel.getEventSeries(eventId)
            .observe(viewLifecycleOwner, Observer {
                val seriesList = view.findViewById<LinearLayout>(R.id.series_list)
                HorizontalListUtils.initHorizontalList(
                    seriesList,
                    it,
                    "Series:",
                    this
                )
            })

        eventDetailsViewModel.getEventStories(eventId)
            .observe(viewLifecycleOwner, Observer {
                val storyList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    storyList,
                    it,
                    "Stories:",
                    this
                )
            })

        eventDetailsViewModel.getEventCreators(eventId)
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
            HorizontalListUtils.SERIES -> {
                bundle.putInt("SERIES_ID", item.id)
                findNavController().navigate(R.id.seriesDetailsFragment, bundle)
            }
            HorizontalListUtils.STORY -> {
                bundle.putInt("STORY_ID", item.id)
                findNavController().navigate(R.id.storyDetailsFragment, bundle)
            }
            HorizontalListUtils.CREATOR -> {
                bundle.putInt("CREATOR_ID", item.id)
                findNavController().navigate(R.id.creatorDetailsFragment, bundle)
            }
        }
    }
}