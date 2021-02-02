package com.example.marvelworld.eventdetails.views

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
import com.example.marvelworld.eventdetails.respository.EventDetailsRepository
import com.example.marvelworld.eventdetails.viewmodel.EventDetailsViewModel
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListFragment
import com.example.marvelworld.util.ResourceType
import kotlin.properties.Delegates

class EventDetailsFragment : Fragment() {

    private lateinit var eventDetailsViewModel: EventDetailsViewModel
    private var eventId by Delegates.notNull<Int>()
    private val horizontalListFragmentMap = mutableMapOf<ResourceType, HorizontalListFragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventId = requireArguments().getInt("EVENT_ID")

        eventDetailsViewModel = ViewModelProvider(
            this,
            EventDetailsViewModel.EventDetailsViewModelFactory(
                EventDetailsRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(EventDetailsViewModel::class.java)

        eventDetailsViewModel.getEvent(eventId)
            .observe(viewLifecycleOwner, { event ->
                val card = DetailCard(
                    event.id,
                    event.title,
                    event.thumbnail,
                    event.description,
                    event.urls,
                    ResourceType.EVENT,
                    event.isFavorite
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
            inflateFragment(R.id.series_list, getString(R.string.series), ResourceType.SERIES)
            inflateFragment(R.id.story_list, getString(R.string.stories), ResourceType.STORY)
        }
    }

    private fun inflateFragment(layoutId: Int, title: String, type: ResourceType) {
        val horizontalListFragment =
            HorizontalListFragment(eventDetailsViewModel, title, type, eventId)

        horizontalListFragmentMap[type] = horizontalListFragment

        childFragmentManager.beginTransaction().replace(
            layoutId,
            horizontalListFragment
        ).commit()
    }
}