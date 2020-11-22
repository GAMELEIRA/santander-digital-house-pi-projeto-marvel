package com.example.marvelworld.serieslist.views

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
import com.example.marvelworld.serieslist.repository.SeriesRepository
import com.example.marvelworld.serieslist.viewmodel.SeriesViewModel
import com.example.marvelworld.serieslist.models.Series

class SeriesListFragment : Fragment(), OnSeriesClickListener {
    private val seriesList = mutableListOf<Series>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_series_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_series_list)
        val manager = GridLayoutManager(view.context, 2)
        val seriesListAdapter = SeriesListAdapter(seriesList, this)

        recycler.apply {
            layoutManager = manager
            adapter = seriesListAdapter
        }

        val seriesViewModel = ViewModelProvider(
            this,
            SeriesViewModel.SeriesViewModelFactory(SeriesRepository())
        ).get(SeriesViewModel::class.java)

        seriesViewModel.getSeries().observe(viewLifecycleOwner, Observer {
            seriesList.clear()
            seriesList.addAll(it)
            seriesListAdapter.notifyDataSetChanged()
        })
    }

    override fun onSeriesClick(position: Int) {
        val bundle = bundleOf("SERIES_ID" to seriesList[position].id)
        findNavController().navigate(R.id.seriesDetailsFragment, bundle)
    }
}