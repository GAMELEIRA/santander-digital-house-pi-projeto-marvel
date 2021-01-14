package com.example.marvelworld.storydetails.views

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
import com.example.marvelworld.storydetails.repository.StoryDetailsRepository
import com.example.marvelworld.storydetails.viewmodel.StoryDetailsViewModel
import com.example.marvelworld.util.ResourceType
import kotlin.properties.Delegates

class StoryDetailsFragment : Fragment() {

    private lateinit var storyDetailsViewModel: StoryDetailsViewModel
    private var storyId by Delegates.notNull<Int>()
    private val horizontalListFragmentMap = mutableMapOf<ResourceType, HorizontalListFragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storyId = requireArguments().getInt("STORY_ID")

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

        if (horizontalListFragmentMap.isEmpty()) {
            inflateFragment(
                R.id.character_list,
                getString(R.string.characters),
                ResourceType.CHARACTER
            )
            inflateFragment(R.id.comic_list, getString(R.string.comics), ResourceType.COMIC)
            inflateFragment(R.id.creator_list, getString(R.string.creators), ResourceType.CREATOR)
            inflateFragment(R.id.event_list, getString(R.string.events), ResourceType.EVENT)
            inflateFragment(R.id.series_list, getString(R.string.series), ResourceType.SERIES)
        }
    }

    private fun inflateFragment(layoutId: Int, title: String, type: ResourceType) {
        val horizontalListFragment =
            HorizontalListFragment(storyDetailsViewModel, title, type, storyId)

        horizontalListFragmentMap[type] = horizontalListFragment

        childFragmentManager.beginTransaction().replace(
            layoutId,
            horizontalListFragment
        ).commit()
    }
}