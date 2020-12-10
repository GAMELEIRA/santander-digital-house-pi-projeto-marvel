package com.example.marvelworld.creatorlist.views

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
import com.example.marvelworld.creatorlist.models.Creator
import com.example.marvelworld.creatorlist.respository.CreatorRepository
import com.example.marvelworld.creatorlist.viewmodel.CreatorViewModel
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment

class CreatorListFragment(
    onlyFavorites: Boolean = false
) : Fragment(),
    OnCreatorClickListener,
    CallbackListener {

    private lateinit var creatorViewModel: CreatorViewModel
    private lateinit var creatorListAdapter: CreatorListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val creatorList = mutableListOf<Creator>()
    private var filter = Filter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creator_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_creator_list)

        val manager = GridLayoutManager(view.context, 2)
        creatorListAdapter = CreatorListAdapter(creatorList, this)

        recycler.apply {
            layoutManager = manager
            adapter = creatorListAdapter
        }

        creatorViewModel = ViewModelProvider(
            this,
            CreatorViewModel.CreatorViewModelFactory(CreatorRepository())
        ).get(CreatorViewModel::class.java)

        creatorViewModel.getCreators().observe(viewLifecycleOwner, {
            creatorList.clear()
            creatorList.addAll(it)
            creatorListAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreatorClick(position: Int) {
        val bundle = bundleOf("CREATOR_ID" to creatorList[position].id)
        findNavController().navigate(R.id.creatorDetailsFragment, bundle)
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
                    hasCharacter = false,
                    hasComic = true,
                    hasEvent = true,
                    hasSeries = true,
                    hasCreator = false,
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

        creatorViewModel.applyFilter(this.filter)

        creatorViewModel.getCreators()
            .observe(viewLifecycleOwner, {
                creatorList.clear()
                creatorList.addAll(it)
                creatorListAdapter.notifyDataSetChanged()
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