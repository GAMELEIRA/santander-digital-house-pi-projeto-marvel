package com.example.marvelworld.favorite.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.marvelworld.R
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.favorite.viewmodel.FavoriteViewModel
import com.example.marvelworld.mainviewpager.views.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayout

class FavoritesFragment : Fragment() {

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var noFavoritesScreen: View
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noFavoritesScreen = view.findViewById(R.id.no_favorites_layout)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        favoriteViewModel = ViewModelProvider(
            this,
            FavoriteViewModel.FavoriteViewModelFactory(
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao()),
                view.context
            )
        ).get(FavoriteViewModel::class.java)

        favoriteViewModel.getFavoriteFragments().observe(viewLifecycleOwner, {
            val fragments = mutableListOf<Fragment>()
            val titles = mutableListOf<String>()

            if (it.isNotEmpty()) {
                it.forEach { (t, u) ->
                    titles.add(t)
                    fragments.add(u)
                }

                tabLayout.setupWithViewPager(viewPager)
                viewPager.adapter = MainViewPagerAdapter(fragments, titles, childFragmentManager)

                tabLayout.visibility = View.VISIBLE
                viewPager.visibility = View.VISIBLE
                noFavoritesScreen.visibility = View.GONE
            } else {
                tabLayout.visibility = View.GONE
                viewPager.visibility = View.GONE
                noFavoritesScreen.visibility = View.VISIBLE
            }
        })
    }
}