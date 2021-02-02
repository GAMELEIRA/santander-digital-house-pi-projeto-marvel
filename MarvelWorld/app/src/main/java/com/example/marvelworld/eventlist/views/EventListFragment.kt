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
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment

class EventListFragment(
    private val onlyFavorites: Boolean = false
) : Fragment(),
    OnEventClickListener,
    CallbackListener {

    private lateinit var eventViewModel: EventViewModel
    private lateinit var eventListAdapter: EventListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val eventList = mutableListOf<Event>()
    private var filter = Filter()
    private var loading = false
    private var onPause = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onPause() {
        super.onPause()

        onPause = true
    }

    override fun onResume() {
        super.onResume()
        if (onPause) {
            updateEvents()
            onPause = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!onlyFavorites) setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_event_list)

        val manager = GridLayoutManager(view.context, 2)
        eventListAdapter = EventListAdapter(eventList, this)

        recycler.apply {
            layoutManager = manager
            adapter = eventListAdapter
        }

        eventViewModel = ViewModelProvider(
            this,
            EventViewModel.EventViewModelFactory(
                EventRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(EventViewModel::class.java)

        if (eventList.isEmpty()) getEvents()

        initInfiniteScroll()
    }

    private fun updateEvents() {
        if (!onlyFavorites) {
            eventViewModel.updateEvents(eventList)
                .observe(viewLifecycleOwner, {
                    eventListAdapter.notifyDataSetChanged()
                })
        } else {
            eventViewModel.updateFavoriteEvents(eventList)
                .observe(viewLifecycleOwner, {
                    eventList.removeAll(it)
                    eventListAdapter.notifyDataSetChanged()
                })
        }
    }

    private fun getEvents() {
        loading = true
        if (onlyFavorites) {
            eventViewModel.getFavoriteEvents().observe(viewLifecycleOwner, {
                eventList.addAll(it)
                eventListAdapter.notifyDataSetChanged()
                loading = false
            })
        } else {
            eventViewModel.getEvents().observe(viewLifecycleOwner, {
                eventList.addAll(it)
                eventListAdapter.notifyDataSetChanged()
                loading = false
            })
        }
    }

    private fun initInfiniteScroll() {
        recycler.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val target = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = target.itemCount
                    val lastVisible = target.findLastVisibleItemPosition()

                    val total = if (!onlyFavorites) {
                        eventViewModel.total
                    } else {
                        eventViewModel.totalFavorite
                    }

                    if (totalItemCount - lastVisible < 10
                        && totalItemCount < total
                        && !loading
                    ) {
                        getEvents()
                    }
                }
            })
        }
    }

    override fun onEventClick(position: Int) {
        val bundle = bundleOf("EVENT_ID" to eventList[position].id)
        findNavController().navigate(R.id.eventDetailsFragment, bundle)
    }

    override fun onEventFavoriteClick(position: Int) {
        eventViewModel.isFavorite(eventList[position].id)
            .observe(viewLifecycleOwner, { isFavorite ->
                if (isFavorite) {
                    eventViewModel.removeFavorite(eventList[position].id)
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                eventList[position].isFavorite = false
                                if (onlyFavorites) {
                                    eventList.removeAt(position)
                                    eventListAdapter.notifyDataSetChanged()
                                } else {
                                    eventListAdapter.notifyItemChanged(position)
                                }
                            }
                        })
                } else {
                    val event = eventList[position]
                    eventViewModel.addFavorite(
                        event.id,
                        event.title,
                        event.thumbnail.path,
                        event.thumbnail.extension
                    )
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                eventList[position].isFavorite = true
                                eventListAdapter.notifyItemChanged(position)
                            }
                        })
                }

                eventList[position].isFavorite = !eventList[position].isFavorite
            })
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
        eventList.clear()
        getEvents()
    }

    private fun updateFilterIcon() {
        if (filter.isEmpty()) {
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