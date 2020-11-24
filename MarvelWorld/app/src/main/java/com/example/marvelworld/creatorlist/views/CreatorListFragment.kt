package com.example.marvelworld.creatorlist.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.creatorlist.viewmodel.CreatorViewModel
import com.example.marvelworld.creatorlist.models.Creator
import com.example.marvelworld.creatorlist.respository.CreatorRepository

class CreatorListFragment(onlyFavorites: Boolean = false) : Fragment(), OnCreatorClickListener {
    private val creatorList = mutableListOf<Creator>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_creator_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_creator_list)
        val manager = GridLayoutManager(view.context, 2)
        val creatorListAdapter = CreatorListAdapter(creatorList, this)

        recycler.apply {
            layoutManager = manager
            adapter = creatorListAdapter
        }

        val creatorViewModel = ViewModelProvider(
            this,
            CreatorViewModel.CreatorViewModelFactory(CreatorRepository())
        ).get(CreatorViewModel::class.java)

        creatorViewModel.getCreators().observe(viewLifecycleOwner, Observer {
            creatorList.clear()
            creatorList.addAll(it)
            creatorListAdapter.notifyDataSetChanged()
        })
    }

    override fun onCreatorClick(position: Int) {
        val bundle = bundleOf("CREATOR_ID" to creatorList[position].id)
        findNavController().navigate(R.id.creatorDetailsFragment, bundle)
    }
}