package com.example.marvelworld.creatordetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
import com.example.marvelworld.creatordetails.repository.CreatorDetailsRepository
import com.example.marvelworld.creatordetails.viewmodel.CreatorDetailsViewModel
import com.example.marvelworld.detailcard.models.DetailCard
import com.example.marvelworld.detailcard.views.DetailCardFragment
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListFragment
import com.example.marvelworld.util.ResourceType
import kotlin.properties.Delegates

class CreatorDetailsFragment : Fragment() {

    private lateinit var creatorDetailsViewModel: CreatorDetailsViewModel
    private var creatorId by Delegates.notNull<Int>()
    private val horizontalListFragmentMap = mutableMapOf<ResourceType, HorizontalListFragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creator_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        creatorId = requireArguments().getInt("CREATOR_ID")

        creatorDetailsViewModel = ViewModelProvider(
            this,
            CreatorDetailsViewModel.CreatorDetailsViewModelFactory(
                CreatorDetailsRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(CreatorDetailsViewModel::class.java)

        creatorDetailsViewModel.getCreator(creatorId)
            .observe(viewLifecycleOwner, { creator ->
                val card = DetailCard(
                    creator.id,
                    creator.fullName,
                    creator.thumbnail.getImagePath(Image.LANDSCAPE_INCREDIBLE),
                    creator.thumbnail.getImagePath(Image.FULL_SIZE),
                    null,
                    creator.urls,
                    ResourceType.CREATOR,
                    creator.isFavorite
                )

                childFragmentManager.beginTransaction().replace(
                    R.id.detail_card,
                    DetailCardFragment(card)
                ).commit()
            })

        if (horizontalListFragmentMap.isEmpty()) {
            inflateFragment(R.id.comic_list, getString(R.string.comics), ResourceType.COMIC)
            inflateFragment(R.id.event_list, getString(R.string.events), ResourceType.EVENT)
            inflateFragment(R.id.series_list, getString(R.string.series), ResourceType.SERIES)
            inflateFragment(R.id.story_list, getString(R.string.stories), ResourceType.STORY)
        }
    }

    private fun inflateFragment(layoutId: Int, title: String, type: ResourceType) {
        val horizontalListFragment =
            HorizontalListFragment(creatorDetailsViewModel, title, type, creatorId)

        horizontalListFragmentMap[type] = horizontalListFragment

        childFragmentManager.beginTransaction().replace(
            layoutId,
            horizontalListFragment
        ).commit()
    }
}