package com.example.marvelworld.creatordetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marvelworld.R
import com.example.marvelworld.creatordetails.repository.CreatorDetailsRepository
import com.example.marvelworld.creatordetails.viewmodel.CreatorDetailsViewModel
import com.example.marvelworld.reusablecomponents.expandablecard.Card
import com.example.marvelworld.reusablecomponents.expandablecard.ExpandableCardUtils
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener

class CreatorDetailsFragment : Fragment(), OnHorizontalListItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creator_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val creatorId = requireArguments().getInt("CREATOR_ID")

        val creatorDetailsViewModel = ViewModelProvider(
            this,
            CreatorDetailsViewModel.CreatorDetailsViewModelFactory(CreatorDetailsRepository())
        ).get(CreatorDetailsViewModel::class.java)

        creatorDetailsViewModel.getCreator(creatorId)
            .observe(viewLifecycleOwner, Observer { creator ->
                val card = Card(
                    creator.fullName,
                    creator.thumbnail.getImagePath(),
                    null,
                    creator.urls
                )
                ExpandableCardUtils.initCard(view, card, childFragmentManager)
            })

        creatorDetailsViewModel.getCreatorComics(creatorId)
            .observe(viewLifecycleOwner, Observer {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Comics:",
                    this
                )
            })

        creatorDetailsViewModel.getCreatorEvents(creatorId)
            .observe(viewLifecycleOwner, Observer {
                val eventList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    eventList,
                    it,
                    "Events:",
                    this
                )
            })

        creatorDetailsViewModel.getCreatorSeries(creatorId)
            .observe(viewLifecycleOwner, Observer {
                val seriesList = view.findViewById<LinearLayout>(R.id.series_list)
                HorizontalListUtils.initHorizontalList(
                    seriesList,
                    it,
                    "Series:",
                    this
                )
            })

        creatorDetailsViewModel.getCreatorStories(creatorId)
            .observe(viewLifecycleOwner, Observer {
                val storyList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    storyList,
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