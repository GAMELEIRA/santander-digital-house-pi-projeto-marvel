package com.example.marvelworld.seriesdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.detailcard.models.DetailCard
import com.example.marvelworld.detailcard.views.DetailCardFragment
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListFragment
import com.example.marvelworld.seriesdetails.respository.SeriesDetailsRepository
import com.example.marvelworld.seriesdetails.viewmodel.SeriesDetailsViewModel
import com.example.marvelworld.util.ResourceType
import kotlin.properties.Delegates

class SeriesDetailsFragment : Fragment() {

    private lateinit var seriesDetailsViewModel: SeriesDetailsViewModel
    private var seriesId by Delegates.notNull<Int>()
    private val horizontalListFragmentMap = mutableMapOf<ResourceType, HorizontalListFragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seriesId = requireArguments().getInt("SERIES_ID")

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

        if (horizontalListFragmentMap.isEmpty()) {
            inflateFragment(
                R.id.character_list,
                getString(R.string.characters),
                ResourceType.CHARACTER
            )
            inflateFragment(R.id.comic_list, getString(R.string.comics), ResourceType.COMIC)
            inflateFragment(R.id.creator_list, getString(R.string.creators), ResourceType.CREATOR)
            inflateFragment(R.id.event_list, getString(R.string.events), ResourceType.EVENT)
            inflateFragment(R.id.story_list, getString(R.string.stories), ResourceType.STORY)
        }
    }

    private fun inflateFragment(layoutId: Int, title: String, type: ResourceType) {
        val horizontalListFragment =
            HorizontalListFragment(seriesDetailsViewModel, title, type, seriesId)

        horizontalListFragmentMap[type] = horizontalListFragment

        childFragmentManager.beginTransaction().replace(
            layoutId,
            horizontalListFragment
        ).commit()
    }
}