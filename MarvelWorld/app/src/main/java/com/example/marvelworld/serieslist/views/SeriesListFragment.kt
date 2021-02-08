package com.example.marvelworld.serieslist.views

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment
import com.example.marvelworld.serieslist.models.Series
import com.example.marvelworld.serieslist.repository.SeriesRepository
import com.example.marvelworld.serieslist.viewmodel.SeriesViewModel

class SeriesListFragment(
    private val onlyFavorites: Boolean = false
) : Fragment(),
    OnSeriesClickListener,
    CallbackListener {

    private lateinit var noResultScreen: View
    private lateinit var seriesViewModel: SeriesViewModel
    private lateinit var seriesListAdapter: SeriesListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val seriesList = mutableListOf<Series?>()
    private var filter = Filter()
    private var loading = false
    private var onPause = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_list, container, false)
    }

    override fun onPause() {
        super.onPause()

        onPause = true
    }

    override fun onResume() {
        super.onResume()
        if (onPause) {
            updateSeries()
            onPause = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noResultScreen = view.findViewById(R.id.no_result_found_layout)

        if (!onlyFavorites) setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_series_list)
        val manager = GridLayoutManager(view.context, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (seriesList[position] != null) {
                    1
                } else {
                    2
                }
            }
        }

        seriesListAdapter = SeriesListAdapter(seriesList, this)

        recycler.apply {
            layoutManager = manager
            adapter = seriesListAdapter
        }

        seriesViewModel = ViewModelProvider(
            this,
            SeriesViewModel.SeriesViewModelFactory(
                SeriesRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(SeriesViewModel::class.java)

        if (seriesList.isEmpty()) getSeries()

        initInfiniteScroll()

        noResultScreen.findViewById<Button>(R.id.clear_filter_button).setOnClickListener {
            onDataReceived(Filter())
        }
    }

    private fun updateSeries() {
        if (!onlyFavorites) {
            seriesViewModel.updateSeries(seriesList)
                .observe(viewLifecycleOwner, {
                    seriesListAdapter.notifyDataSetChanged()
                })
        } else {
            seriesViewModel.updateFavoriteSeries(seriesList)
                .observe(viewLifecycleOwner, {
                    seriesList.removeAll(it)
                    seriesListAdapter.notifyDataSetChanged()
                })
        }
    }

    private fun getSeries() {
        loading = true
        seriesListAdapter.addNullData()
        if (onlyFavorites) {
            seriesViewModel.getFavoriteSeries().observe(viewLifecycleOwner, {
                seriesListAdapter.removeNull()
                seriesList.addAll(it)
                seriesListAdapter.notifyDataSetChanged()
                loading = false
            })
        } else {
            seriesViewModel.getSeries().observe(viewLifecycleOwner, {
                seriesListAdapter.removeNull()
                seriesList.addAll(it)
                seriesListAdapter.notifyDataSetChanged()
                testNoResult()
                loading = false
            })
        }
    }

    private fun testNoResult() {
        if (seriesList.isEmpty()) {
            recycler.visibility = View.GONE
            noResultScreen.visibility = View.VISIBLE
        } else {
            recycler.visibility = View.VISIBLE
            noResultScreen.visibility = View.GONE
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
                        seriesViewModel.total
                    } else {
                        seriesViewModel.totalFavorite
                    }

                    if (totalItemCount - lastVisible < 10
                        && totalItemCount < total
                        && !loading
                    ) {
                        getSeries()
                    }
                }
            })
        }
    }

    override fun onSeriesClick(position: Int) {
        val bundle = bundleOf("SERIES_ID" to seriesList[position]!!.id)
        findNavController().navigate(R.id.seriesDetailsFragment, bundle)
    }

    override fun onSeriesFavoriteClick(position: Int) {
        seriesViewModel.isFavorite(seriesList[position]!!.id)
            .observe(viewLifecycleOwner, { isFavorite ->
                if (isFavorite) {
                    seriesViewModel.removeFavorite(seriesList[position]!!.id)
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                seriesList[position]!!.isFavorite = false
                                if (onlyFavorites) {
                                    seriesList.removeAt(position)
                                    seriesListAdapter.notifyDataSetChanged()
                                } else {
                                    seriesListAdapter.notifyItemChanged(position)
                                }
                            }
                        })
                } else {
                    val series = seriesList[position]
                    seriesViewModel.addFavorite(
                        series!!.id,
                        series.title,
                        series.thumbnail.path,
                        series.thumbnail.extension
                    )
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                seriesList[position]!!.isFavorite = true
                                seriesListAdapter.notifyItemChanged(position)
                            }
                        })
                }

                seriesList[position]!!.isFavorite = !seriesList[position]!!.isFavorite
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