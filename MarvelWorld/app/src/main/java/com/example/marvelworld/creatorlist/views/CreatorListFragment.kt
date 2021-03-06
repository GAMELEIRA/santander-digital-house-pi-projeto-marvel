package com.example.marvelworld.creatorlist.views

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
import com.example.marvelworld.creatorlist.models.Creator
import com.example.marvelworld.creatorlist.respository.CreatorRepository
import com.example.marvelworld.creatorlist.viewmodel.CreatorViewModel
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment

class CreatorListFragment(
    private val onlyFavorites: Boolean = false
) : Fragment(),
    OnCreatorClickListener,
    CallbackListener {

    private lateinit var noResultScreen: View
    private lateinit var creatorViewModel: CreatorViewModel
    private lateinit var creatorListAdapter: CreatorListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val creatorList = mutableListOf<Creator?>()
    private var filter = Filter()
    private var loading = false
    private var onPause = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creator_list, container, false)
    }

    override fun onPause() {
        super.onPause()

        onPause = true
    }

    override fun onResume() {
        super.onResume()
        if (onPause) {
            updateCreators()
            onPause = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noResultScreen = view.findViewById(R.id.no_result_found_layout)

        if (!onlyFavorites) setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_creator_list)

        val manager = GridLayoutManager(view.context, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (creatorList[position] != null) {
                    1
                } else {
                    2
                }
            }
        }

        creatorListAdapter = CreatorListAdapter(creatorList, this)

        recycler.apply {
            layoutManager = manager
            adapter = creatorListAdapter
        }

        creatorViewModel = ViewModelProvider(
            this,
            CreatorViewModel.CreatorViewModelFactory(
                CreatorRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(CreatorViewModel::class.java)

        if (creatorList.isEmpty()) getCreators()

        initInfiniteScroll()

        noResultScreen.findViewById<Button>(R.id.clear_filter_button).setOnClickListener {
            onDataReceived(Filter())
        }
    }

    private fun updateCreators() {
        if (!onlyFavorites) {
            creatorViewModel.updateCreators(creatorList)
                .observe(viewLifecycleOwner, {
                    creatorListAdapter.notifyDataSetChanged()
                })
        } else {
            creatorViewModel.updateFavoriteCreators(creatorList)
                .observe(viewLifecycleOwner, {
                    creatorList.removeAll(it)
                    creatorListAdapter.notifyDataSetChanged()
                })
        }
    }

    private fun getCreators() {
        loading = true
        creatorListAdapter.addNullData()
        if (onlyFavorites) {
            creatorViewModel.getFavoriteCreators().observe(viewLifecycleOwner, {
                creatorListAdapter.removeNull()
                creatorList.addAll(it)
                creatorListAdapter.notifyDataSetChanged()
                loading = false
            })
        } else {
            creatorViewModel.getCreators().observe(viewLifecycleOwner, {
                creatorListAdapter.removeNull()
                creatorList.addAll(it)
                creatorListAdapter.notifyDataSetChanged()
                testNoResult()
                loading = false
            })
        }
    }

    private fun testNoResult() {
        if (creatorList.isEmpty()) {
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
                        creatorViewModel.total
                    } else {
                        creatorViewModel.totalFavorite
                    }

                    if (totalItemCount - lastVisible < 10
                        && totalItemCount < total
                        && !loading
                    ) {
                        getCreators()
                    }
                }
            })
        }
    }

    override fun onCreatorClick(position: Int) {
        val bundle = bundleOf("CREATOR_ID" to creatorList[position]!!.id)
        findNavController().navigate(R.id.creatorDetailsFragment, bundle)
    }

    override fun onCreatorFavoriteClick(position: Int) {
        creatorViewModel.isFavorite(creatorList[position]!!.id)
            .observe(viewLifecycleOwner, { isFavorite ->
                if (isFavorite) {
                    creatorViewModel.removeFavorite(creatorList[position]!!.id)
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                creatorList[position]!!.isFavorite = false
                                if (onlyFavorites) {
                                    creatorList.removeAt(position)
                                    creatorListAdapter.notifyDataSetChanged()
                                } else {
                                    creatorListAdapter.notifyItemChanged(position)
                                }
                            }
                        })
                } else {
                    val creator = creatorList[position]!!
                    creatorViewModel.addFavorite(
                        creator.id,
                        creator.fullName,
                        creator.thumbnail.path,
                        creator.thumbnail.extension
                    )
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                creatorList[position]!!.isFavorite = true
                                creatorListAdapter.notifyItemChanged(position)
                            }
                        })
                }

                creatorList[position]!!.isFavorite = !creatorList[position]!!.isFavorite
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
        creatorList.clear()
        getCreators()
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