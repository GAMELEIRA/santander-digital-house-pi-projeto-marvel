package com.example.marvelworld.serieslist.views

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
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment
import com.example.marvelworld.serieslist.models.Series
import com.example.marvelworld.serieslist.repository.SeriesRepository
import com.example.marvelworld.serieslist.viewmodel.SeriesViewModel

class SeriesListFragment(
    onlyFavorites: Boolean = false
) : Fragment(),
    OnSeriesClickListener,
    CallbackListener {
    private lateinit var seriesViewModel: SeriesViewModel
    private lateinit var seriesListAdapter: SeriesListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val seriesList = mutableListOf<Series>()
    private var filter = Filter()
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_series_list)
        val manager = GridLayoutManager(view.context, 2)
        seriesListAdapter = SeriesListAdapter(seriesList, this)

        recycler.apply {
            layoutManager = manager
            adapter = seriesListAdapter
        }

        seriesViewModel = ViewModelProvider(
            this,
            SeriesViewModel.SeriesViewModelFactory(SeriesRepository())
        ).get(SeriesViewModel::class.java)

        if (seriesList.isEmpty()) getSeries()

        initInfiniteScroll()
    }

    private fun getSeries() {
        loading = true
        seriesViewModel.getSeries().observe(viewLifecycleOwner, {
            seriesList.addAll(it)
            seriesListAdapter.notifyDataSetChanged()
            loading = false
        })
    }

    private fun initInfiniteScroll() {
        recycler.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val target = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = target.itemCount
                    val lastVisible = target.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisible < 10
                        && totalItemCount < seriesViewModel.total
                        && !loading
                    ) {
                        getSeries()
                    }
                }
            })
        }
    }

    override fun onSeriesClick(position: Int) {
        val bundle = bundleOf("SERIES_ID" to seriesList[position].id)
        findNavController().navigate(R.id.seriesDetailsFragment, bundle)
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
                    hasComic = true,
                    hasEvent = true,
                    hasSeries = false,
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
        seriesViewModel.applyFilter(this.filter)
        seriesList.clear()
        getSeries()
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