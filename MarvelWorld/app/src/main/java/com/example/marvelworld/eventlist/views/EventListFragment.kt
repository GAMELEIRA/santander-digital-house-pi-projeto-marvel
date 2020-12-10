package com.example.marvelworld.eventlist.views

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.eventlist.repository.EventRepository
import com.example.marvelworld.eventlist.viewmodel.EventViewModel
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment

class EventListFragment(
    onlyFavorites: Boolean = false
) : Fragment(),
    OnEventClickListener,
    CallbackListener {

    private lateinit var eventViewModel: EventViewModel
    private lateinit var eventListAdapter: EventListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val eventList = mutableListOf<Event>()
    private var filter = Filter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_event_list)

        val manager = GridLayoutManager(view.context, 2)
        eventListAdapter = EventListAdapter(eventList, this)

        recycler.apply {
            layoutManager = manager
            adapter = eventListAdapter
        }

        eventViewModel = ViewModelProvider(
            this,
            EventViewModel.EventViewModelFactory(EventRepository())
        ).get(EventViewModel::class.java)

        eventViewModel.getEvents().observe(viewLifecycleOwner, {
            eventList.clear()
            eventList.addAll(it)
            eventListAdapter.notifyDataSetChanged()
        })
    }

    override fun onEventClick(position: Int) {
        val bundle = bundleOf("EVENT_ID" to eventList[position].id)
        findNavController().navigate(R.id.eventDetailsFragment, bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        filterIcon = menu.findItem(R.id.filtersFragment)

        updateFilterIcon()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            filterIcon.itemId -> {
                FilterListFragment(
                    hasName = true,
                    hasTitle = false,
                    hasCharacter = true,
                    hasComic = true,
                    hasEvent = false,
                    hasSeries = true,
                    hasCreator = true,
                    callbackListener = this,
                    filter = filter
                ).show(childFragmentManager, "add_filters")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDataReceived(filter: Filter) {
        this.filter = filter

        updateFilterIcon()

        eventViewModel.applyFilter(this.filter)

        eventViewModel.getEvents()
            .observe(viewLifecycleOwner, {
                eventList.clear()
                eventList.addAll(it)
                eventListAdapter.notifyDataSetChanged()
            })
    }

    private fun updateFilterIcon() {
        if (this.filter.isEmpty()) {
            filterIcon.icon =
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_filter_alt_24px,
                    null
                )
        } else {
            filterIcon.icon =
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_filter_filled_alt_24px,
                    null
                )
        }
    }
}