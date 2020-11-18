package com.example.marvelworld.storylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R

class StoryListFragment : Fragment() {
    private val storyList = mutableListOf<Story>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_story_list)
        val manager = GridLayoutManager(view.context, 2)
        val storyListAdapter = StoryListAdapter(storyList)

        recycler.apply {
            layoutManager = manager
            adapter = storyListAdapter
        }

        val storyViewModel = ViewModelProvider(
            this,
            StoryViewModel.StoryViewModelFactory(StoryRepository())
        ).get(StoryViewModel::class.java)

        storyViewModel.getStories().observe(viewLifecycleOwner, Observer {
            storyList.clear()
            storyList.addAll(it)
            storyListAdapter.notifyDataSetChanged()
        })
    }
}