package com.example.marvelworld.seriesdetails.views

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
import com.example.marvelworld.eventdetails.viewmodel.EventDetailsViewModel
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener
import com.example.marvelworld.seriesdetails.respository.SeriesDetailsRepository
import com.example.marvelworld.seriesdetails.viewmodel.SeriesDetailsViewModel
import com.example.marvelworld.util.ResourceType

class SeriesDetailsFragment :
    Fragment(),
    OnHorizontalListItemClickListener {

    private lateinit var seriesDetailsViewModel: SeriesDetailsViewModel

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

        seriesDetailsViewModel = ViewModelProvider(
            this,
            SeriesDetailsViewModel.SeriesDetailsViewModelFactory(
                SeriesDetailsRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(SeriesDetailsViewModel::class.java)

        seriesDetailsViewModel.getOneSeries(seriesId)
            .observe(viewLifecycleOwner, { series ->
                val card = DetailCard(
                    series.id,
                    series.title,
                    series.thumbnail.getImagePath(Image.LANDSCAPE_INCREDIBLE),
                    series.thumbnail.getImagePath(Image.FULL_SIZE),
                    series.description,
                    series.urls,
                    ResourceType.SERIES,
                    series.isFavorite
                )

                childFragmentManager.beginTransaction().replace(
                    R.id.detail_card,
                    DetailCardFragment(card)
                ).commit()
            })

        seriesDetailsViewModel.getSeriesCharacters(seriesId)
            .observe(viewLifecycleOwner, {
                val characterList = view.findViewById<LinearLayout>(R.id.character_list)
                HorizontalListUtils.initHorizontalList(
                    characterList,
                    it,
                    getString(R.string.characters),
                    this
                )
            })

        seriesDetailsViewModel.getSeriesComics(seriesId)
            .observe(viewLifecycleOwner, {
                val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
                HorizontalListUtils.initHorizontalList(
                    comicList,
                    it,
                    getString(R.string.comics),
                    this
                )
            })

        seriesDetailsViewModel.getSeriesStories(seriesId)
            .observe(viewLifecycleOwner, {
                val storyList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    storyList,
                    it,
                    getString(R.string.stories),
                    this
                )
            })

        seriesDetailsViewModel.getSeriesEvents(seriesId)
            .observe(viewLifecycleOwner, {
                val eventList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    eventList,
                    it,
                    getString(R.string.events),
                    this
                )
            })

        seriesDetailsViewModel.getSeriesCreators(seriesId)
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