package com.example.marvelworld.comiclist.views

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
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.comiclist.repository.ComicRepository
import com.example.marvelworld.comiclist.viewmodel.ComicViewModel
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment

class ComicListFragment(
    private val onlyFavorites: Boolean = false
) : Fragment(),
    OnComicClickListener,
    CallbackListener {

    private lateinit var noResultScreen: View
    private lateinit var comicViewModel: ComicViewModel
    private lateinit var comicListAdapter: ComicListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val comicList = mutableListOf<Comic?>()
    private var filter = Filter()
    private var loading = false
    private var onPause = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic_list, container, false)
    }

    override fun onPause() {
        super.onPause()

        onPause = true
    }

    override fun onResume() {
        super.onResume()
        if (onPause) {
            updateComics()
            onPause = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noResultScreen = view.findViewById(R.id.no_result_found_layout)

        if (!onlyFavorites) setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_comic_list)

        val manager = GridLayoutManager(view.context, 2)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (comicList[position] != null) {
                    1
                } else {
                    2
                }
            }
        }

        comicListAdapter = ComicListAdapter(comicList, this)

        recycler.apply {
            layoutManager = manager
            adapter = comicListAdapter
        }

        comicViewModel = ViewModelProvider(
            this,
            ComicViewModel.ComicViewModelFactory(
                ComicRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(ComicViewModel::class.java)

        if (comicList.isEmpty()) getComics()

        initInfiniteScroll()

        noResultScreen.findViewById<Button>(R.id.clear_filter_button).setOnClickListener {
            onDataReceived(Filter())
        }
    }

    private fun updateComics() {
        if (!onlyFavorites) {
            comicViewModel.updateComics(comicList)
                .observe(viewLifecycleOwner, {
                    comicListAdapter.notifyDataSetChanged()
                })
        } else {
            comicViewModel.updateFavoriteComics(comicList)
                .observe(viewLifecycleOwner, {
                    comicList.removeAll(it)
                    comicListAdapter.notifyDataSetChanged()
                })
        }
    }

    private fun getComics() {
        loading = true
        comicListAdapter.addNullData()
        if (onlyFavorites) {
            comicViewModel.getFavoriteComics().observe(viewLifecycleOwner, {
                comicListAdapter.removeNull()
                comicList.addAll(it)
                comicListAdapter.notifyDataSetChanged()
                loading = false
            })
        } else {
            comicViewModel.getComics().observe(viewLifecycleOwner, {
                comicListAdapter.removeNull()
                comicList.addAll(it)
                comicListAdapter.notifyDataSetChanged()
                testNoResult()
                loading = false
            })
        }
    }

    private fun testNoResult() {
        if (comicList.isEmpty()) {
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
                        comicViewModel.total
                    } else {
                        comicViewModel.totalFavorite
                    }

                    if (totalItemCount - lastVisible < 10
                        && totalItemCount < total
                        && !loading
                    ) {
                        getComics()
                    }
                }
            })
        }
    }

    override fun onComicClick(position: Int) {
        val bundle = bundleOf("COMIC_ID" to comicList[position]!!.id)
        findNavController().navigate(R.id.comicDetailsFragment, bundle)
    }

    override fun onComicFavoriteClick(position: Int) {
        comicViewModel.isFavorite(comicList[position]!!.id)
            .observe(viewLifecycleOwner, { isFavorite ->
                if (isFavorite) {
                    comicViewModel.removeFavorite(comicList[position]!!.id)
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                comicList[position]!!.isFavorite = false
                                if (onlyFavorites) {
                                    comicList.removeAt(position)
                                    comicListAdapter.notifyDataSetChanged()
                                } else {
                                    comicListAdapter.notifyItemChanged(position)
                                }
                            }
                        })
                } else {
                    val comic = comicList[position]!!
                    comicViewModel.addFavorite(
                        comic.id,
                        comic.title,
                        comic.thumbnail.path,
                        comic.thumbnail.extension
                    )
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                comicList[position]!!.isFavorite = true
                                comicListAdapter.notifyItemChanged(position)
                            }
                        })
                }

                comicList[position]!!.isFavorite = !comicList[position]!!.isFavorite
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
        comicList.clear()
        getComics()
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