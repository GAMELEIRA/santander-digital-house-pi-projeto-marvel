package com.example.marvelworld.comiclist.views

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
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.comiclist.repository.ComicRepository
import com.example.marvelworld.comiclist.viewmodel.ComicViewModel
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment

class ComicListFragment(
    onlyFavorites: Boolean = false
) : Fragment(),
    OnComicClickListener,
    CallbackListener {

    private lateinit var comicViewModel: ComicViewModel
    private lateinit var comicListAdapter: ComicListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val comicList = mutableListOf<Comic>()
    private var filter = Filter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_comic_list)

        val manager = GridLayoutManager(view.context, 2)
        comicListAdapter = ComicListAdapter(comicList, this)

        recycler.apply {
            layoutManager = manager
            adapter = comicListAdapter
        }

        comicViewModel = ViewModelProvider(
            this,
            ComicViewModel.ComicViewModelFactory(ComicRepository())
        ).get(ComicViewModel::class.java)

        comicViewModel.getComics().observe(viewLifecycleOwner, {
            comicList.clear()
            comicList.addAll(it)
            comicListAdapter.notifyDataSetChanged()
        })
    }

    override fun onComicClick(position: Int) {
        val bundle = bundleOf("COMIC_ID" to comicList[position].id)
        findNavController().navigate(R.id.comicDetailsFragment, bundle)
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
                    hasName = false,
                    hasTitle = true,
                    hasCharacter = true,
                    hasComic = false,
                    hasEvent = true,
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

        comicViewModel.applyFilter(this.filter)

        comicViewModel.getComics()
            .observe(viewLifecycleOwner, {
                comicList.clear()
                comicList.addAll(it)
                comicListAdapter.notifyDataSetChanged()
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