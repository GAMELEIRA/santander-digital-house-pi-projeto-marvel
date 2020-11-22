package com.example.marvelworld.seriesdetails.views

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
import com.example.marvelworld.seriesdetails.viewmodel.SeriesDetailsViewModel
import com.example.marvelworld.seriesdetails.respository.SeriesDetailsRepository

class SeriesDetailsFragment : Fragment(), OnHorizontalListItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seriesId = requireArguments().getInt("SERIES_ID")

        val seriesDetailsViewModel = ViewModelProvider(
            this,
            SeriesDetailsViewModel.SeriesDetailsViewModelFactory(SeriesDetailsRepository())
        ).get(SeriesDetailsViewModel::class.java)

        seriesDetailsViewModel.getOneSeries(seriesId)
            .observe(viewLifecycleOwner, Observer { series ->
                val card = Card(
                    series.title,
                    series.thumbnail.getImagePath(),
                    series.description,
                    series.urls
                )
                ExpandableCardUtils.initCard(view, card, childFragmentManager)
            })

        seriesDetailsViewModel.getSeriesCharacters(seriesId)
            .observe(viewLifecycleOwner, Observer {
                val characterList = view.findViewById<LinearLayout>(R.id.character_list)
                HorizontalListUtils.initHorizontalList(
                    characterList,
                    it,
                    "Characters:",
                    this
                )
            })

        seriesDetailsViewModel.getSeriesComics(seriesId)
            .observe(viewLifecycleOwner, Observer {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    "Comics:",
                    this
                )
            })

        seriesDetailsViewModel.getSeriesStories(seriesId)
            .observe(viewLifecycleOwner, Observer {
                val storyList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    storyList,
                    it,
                    "Stories:",
                    this
                )
            })

        seriesDetailsViewModel.getSeriesEvents(seriesId)
            .observe(viewLifecycleOwner, Observer {
                val eventList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    eventList,
                    it,
                    "Events:",
                    this
                )
            })

        seriesDetailsViewModel.getSeriesCreators(seriesId)
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
            else -> Toast.makeText(this.context, "cliquei", Toast.LENGTH_SHORT).show()
        }
    }
}