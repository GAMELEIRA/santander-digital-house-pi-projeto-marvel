package com.example.marvelworld.comiclist.views

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
import com.example.marvelworld.comiclist.models.Comic
import com.example.marvelworld.comiclist.repository.ComicRepository
import com.example.marvelworld.comiclist.viewmodel.ComicViewModel

class ComicListFragment : Fragment() {
    private val comicList = mutableListOf<Comic>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_comic_list)
        val manager = GridLayoutManager(view.context, 2)
        val comicListAdapter = ComicListAdapter(comicList)

        recycler.apply {
            layoutManager = manager
            adapter = comicListAdapter
        }

        val comicViewModel = ViewModelProvider(
            this,
            ComicViewModel.ComicViewModelFactory(ComicRepository())
        ).get(ComicViewModel::class.java)

        comicViewModel.getComics().observe(viewLifecycleOwner, Observer {
            comicList.clear()
            comicList.addAll(it)
            comicListAdapter.notifyDataSetChanged()
        })
    }
}