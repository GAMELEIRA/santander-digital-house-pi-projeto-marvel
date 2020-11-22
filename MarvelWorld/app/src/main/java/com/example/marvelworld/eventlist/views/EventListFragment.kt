package com.example.marvelworld.eventlist.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.eventlist.repository.EventRepository
import com.example.marvelworld.eventlist.viewmodel.EventViewModel
import com.example.marvelworld.eventlist.models.Event
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener

class EventListFragment : Fragment(), OnEventClickListener {
    private val eventList = mutableListOf<Event>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_event_list)
        val manager = GridLayoutManager(view.context, 2)
        val eventListAdapter = EventListAdapter(eventList, this)

        recycler.apply {
            layoutManager = manager
            adapter = eventListAdapter
        }

        val eventViewModel = ViewModelProvider(
            this,
            EventViewModel.EventViewModelFactory(EventRepository())
        ).get(EventViewModel::class.java)

        eventViewModel.getEvents().observe(viewLifecycleOwner, Observer {
            eventList.clear()
            eventList.addAll(it)
            eventListAdapter.notifyDataSetChanged()
        })
    }

    override fun onEventClick(position: Int) {
        val bundle = bundleOf("EVENT_ID" to eventList[position].id)
        findNavController().navigate(R.id.eventDetailsFragment, bundle)
    }
}