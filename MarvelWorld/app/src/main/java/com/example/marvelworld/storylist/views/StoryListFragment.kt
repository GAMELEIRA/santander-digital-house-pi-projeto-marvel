package com.example.marvelworld.storylist.views

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment
import com.example.marvelworld.storylist.models.Story
import com.example.marvelworld.storylist.repository.StoryRepository
import com.example.marvelworld.storylist.viewmodel.StoryViewModel

class StoryListFragment(
    onlyFavorites: Boolean = false
) : Fragment(),
    OnStoryClickListener,
    CallbackListener {

    private lateinit var storyViewModel: StoryViewModel
    private lateinit var storyListAdapter: StoryListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private val storyList = mutableListOf<Story>()
    private var filter = Filter()
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_story_list)
        val manager = LinearLayoutManager(view.context)
        storyListAdapter = StoryListAdapter(storyList, this)

        recycler.apply {
            layoutManager = manager
            adapter = storyListAdapter
        }

        storyViewModel = ViewModelProvider(
            this,
            StoryViewModel.StoryViewModelFactory(StoryRepository())
        ).get(StoryViewModel::class.java)

        getStories()

        initInfiniteScroll()
    }

    private fun getStories() {
        loading = true
        storyViewModel.getStories().observe(viewLifecycleOwner, {
            storyList.addAll(it)
            storyListAdapter.notifyDataSetChanged()
            loading = false
        })
    }

    private fun initInfiniteScroll() {
        recycler.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val target = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = target.itemCount
                    val lastVisible = target.findLastVisibleItemPosition()

                    if (totalItemCount - lastVisible < 10
                        && totalItemCount < storyViewModel.total
                        && !loading
                    ) {
                        getStories()
                    }
                }
            })
        }
    }

    override fun onStoryClick(position: Int) {
        val bundle = bundleOf("STORY_ID" to storyList[position].id)
        findNavController().navigate(R.id.storyDetailsFragment, bundle)
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
                    hasTitle = false,
                    hasCharacter = true,
                    hasComic = true,
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
        storyViewModel.applyFilter(this.filter)
        storyList.clear()
        getStories()
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