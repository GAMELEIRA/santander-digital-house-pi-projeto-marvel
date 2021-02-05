package com.example.marvelworld.horizontallist

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.util.ResourceType

class HorizontalListFragment(
    private val horizontalListViewModel: InfiniteScrollable,
    private val title: String,
    private val type: ResourceType,
    private val resourceId: Int
) : Fragment(), OnHorizontalListItemClickListener {

    private lateinit var titleView: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var itemsAdapter: HorizontalListAdapter
    private val items = mutableListOf<HorizontalListItem>()
    private var loading = false
    private lateinit var myView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_horizontal_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myView = view

        view.visibility = View.GONE

        titleView = view.findViewById(R.id.horizontal_list_title)
        recycler = view.findViewById(R.id.horizontal_list)

        titleView.text = title

        val manager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        itemsAdapter = HorizontalListAdapter(items, this)

        recycler.apply {
            adapter = itemsAdapter
            layoutManager = manager
            addItemDecoration(HorizontalListDecoration(items, 25))
        }

        if (items.isEmpty()) {
            getItems()
        } else {
            view.visibility = View.VISIBLE
        }

        initInfiniteScroll()
    }

    override fun onHorizontalListItemClick(item: HorizontalListItem) {
        val bundle = bundleOf()
        when (item.type) {
            ResourceType.CHARACTER -> {
                bundle.putInt("CHARACTER_ID", item.id)
                findNavController().navigate(R.id.characterDetailsFragment, bundle)
            }
            ResourceType.COMIC -> {
                bundle.putInt("COMIC_ID", item.id)
                findNavController().navigate(R.id.comicDetailsFragment, bundle)
            }
            ResourceType.CREATOR -> {
                bundle.putInt("CREATOR_ID", item.id)
                findNavController().navigate(R.id.creatorDetailsFragment, bundle)
            }
            ResourceType.EVENT -> {
                bundle.putInt("EVENT_ID", item.id)
                findNavController().navigate(R.id.eventDetailsFragment, bundle)
            }
            ResourceType.SERIES -> {
                bundle.putInt("SERIES_ID", item.id)
                findNavController().navigate(R.id.seriesDetailsFragment, bundle)
            }
            ResourceType.STORY -> {
                bundle.putInt("STORY_ID", item.id)
                findNavController().navigate(R.id.storyDetailsFragment, bundle)
            }
        }
    }

    private fun getItems() {
        loading = true
        when (type) {
            ResourceType.CHARACTER -> horizontalListViewModel.getCharacters(resourceId)
                .observe(viewLifecycleOwner, { addItems(it) })
            ResourceType.COMIC -> horizontalListViewModel.getComics(resourceId)
                .observe(viewLifecycleOwner, { addItems(it) })
            ResourceType.CREATOR -> horizontalListViewModel.getCreators(resourceId)
                .observe(viewLifecycleOwner, { addItems(it) })
            ResourceType.EVENT -> horizontalListViewModel.getEvents(resourceId)
                .observe(viewLifecycleOwner, { addItems(it) })
            ResourceType.SERIES -> horizontalListViewModel.getSeries(resourceId)
                .observe(viewLifecycleOwner, { addItems(it) })
            ResourceType.STORY -> horizontalListViewModel.getStories(resourceId)
                .observe(viewLifecycleOwner, { addItems(it) })
        }
    }

    private fun addItems(it: List<HorizontalListItem>) {
        items.addAll(it)
        itemsAdapter.notifyDataSetChanged()
        loading = false
        if (items.isNotEmpty()) myView.visibility = View.VISIBLE
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
                        && totalItemCount < horizontalListViewModel.total
                        && !loading
                    ) {
                        getItems()
                    }
                }
            })
        }
    }

    private class HorizontalListDecoration(
        private val list: List<HorizontalListItem>,
        private val margin: Int
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildLayoutPosition(view)
            if (position < list.size - 1) outRect.right = margin
        }
    }
}