package com.example.marvelworld.mainviewpager.views

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.marvelworld.R
import com.example.marvelworld.characterlist.views.CharacterListFragment
import com.example.marvelworld.comiclist.views.ComicListFragment
import com.example.marvelworld.creatorlist.views.CreatorListFragment
import com.example.marvelworld.eventlist.views.EventListFragment
import com.example.marvelworld.home.views.OnFilterListener
import com.example.marvelworld.serieslist.views.SeriesListFragment
import com.example.marvelworld.storylist.views.StoryListFragment
import com.google.android.material.tabs.TabLayout

class MainViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_view_pager, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = listOf(
            CharacterListFragment(),
            ComicListFragment(),
            EventListFragment(),
            SeriesListFragment(),
            StoryListFragment(),
            CreatorListFragment()
        )

        val titles = listOf("Characters", "Comics", "Events", "Series", "Stories", "Creators")

        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        tabLayout.setupWithViewPager(viewPager)

        viewPager.adapter = MainViewPagerAdapter(fragments, titles, childFragmentManager)
    }

    override fun onResume() {
        super.onResume()
        val onFilterListener = context as OnFilterListener
        onFilterListener.showFilterIcon()
    }

    override fun onPause() {
        super.onPause()
        val onFilterListener = context as OnFilterListener
        onFilterListener.hideFilterIton()
    }
}