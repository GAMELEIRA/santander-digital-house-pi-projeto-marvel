package com.example.marvelworld.filters.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.marvelworld.R
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.models.FilterServiceResponse
import com.example.marvelworld.filters.viewmodel.FilterListViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class FilterListFragment(
    private val hasName: Boolean = true,
    private val hasTitle: Boolean = true,
    private val hasCharacter: Boolean = true,
    private val hasComic: Boolean = true,
    private val hasEvent: Boolean = true,
    private val hasSeries: Boolean = true,
    private val hasCreator: Boolean = true,
    private val callbackListener: CallbackListener,
    private var filter: Filter
) : DialogFragment() {
    private var text: String? = null
    private var characters = mutableListOf<FilterServiceResponse>()
    private var comics = mutableListOf<FilterServiceResponse>()
    private var events = mutableListOf<FilterServiceResponse>()
    private var series = mutableListOf<FilterServiceResponse>()
    private var creators = mutableListOf<FilterServiceResponse>()

    private val filterListViewModel: FilterListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        return inflater.inflate(R.layout.fragment_filter_list, container, false)
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameFilterLayout = view.findViewById<TextInputLayout>(R.id.name_filter_layout)
        val nameFilter = view.findViewById<TextInputEditText>(R.id.name_filter)
        val titleFilterLayout = view.findViewById<TextInputLayout>(R.id.title_filter_layout)
        val titleFilter = view.findViewById<TextInputEditText>(R.id.title_filter)
        val characterFilter = view.findViewById<FrameLayout>(R.id.character_filter)
        val comicFilter = view.findViewById<FrameLayout>(R.id.comic_filter)
        val eventFilter = view.findViewById<FrameLayout>(R.id.event_filter)
        val seriesFilter = view.findViewById<FrameLayout>(R.id.series_filter)
        val creatorFilter = view.findViewById<FrameLayout>(R.id.creator_filter)
        val filterButton = view.findViewById<Button>(R.id.filter_button)
        val closeFilters = view.findViewById<ImageButton>(R.id.close_filters)
        val clearFilters = view.findViewById<TextView>(R.id.clear_filters)

        val fragmentMap = hashMapOf<Int, FilterFragment>()

        if (hasName) {
            nameFilterLayout.visibility = View.VISIBLE
            filter.text.also { nameFilter.setText(it) }
        }

        if (hasTitle) {
            titleFilterLayout.visibility = View.VISIBLE
            filter.text.also { titleFilter.setText(it) }
        }

        if (hasCharacter) {
            val fragment = FilterFragment(
                Filter.CHARACTER,
                filter.filterMap[Filter.CHARACTER]!!
            )
            fragmentMap[Filter.CHARACTER] = fragment

            childFragmentManager
                .beginTransaction()
                .replace(
                    R.id.character_filter,
                    fragment
                ).commit()
            characterFilter.visibility = View.VISIBLE

            filterListViewModel.characters.observe(viewLifecycleOwner, {
                characters.clear()
                characters.addAll(it)
            })
        }

        if (hasComic) {
            val fragment = FilterFragment(
                Filter.COMIC,
                filter.filterMap[Filter.COMIC]!!
            )
            fragmentMap[Filter.COMIC] = fragment

            childFragmentManager
                .beginTransaction()
                .replace(
                    R.id.comic_filter,
                    fragment
                ).commit()
            comicFilter.visibility = View.VISIBLE

            filterListViewModel.comics.observe(viewLifecycleOwner, {
                comics.clear()
                comics.addAll(it)
            })
        }

        if (hasEvent) {
            val fragment = FilterFragment(
                Filter.EVENT,
                filter.filterMap[Filter.EVENT]!!
            )
            fragmentMap[Filter.EVENT] = fragment

            childFragmentManager
                .beginTransaction()
                .replace(
                    R.id.event_filter,
                    fragment
                ).commit()
            eventFilter.visibility = View.VISIBLE

            filterListViewModel.events.observe(viewLifecycleOwner, {
                events.clear()
                events.addAll(it)
            })
        }

        if (hasSeries) {
            val fragment = FilterFragment(
                Filter.SERIES,
                filter.filterMap[Filter.SERIES]!!
            )
            fragmentMap[Filter.SERIES] = fragment

            childFragmentManager
                .beginTransaction()
                .replace(
                    R.id.series_filter,
                    fragment
                ).commit()
            seriesFilter.visibility = View.VISIBLE

            filterListViewModel.series.observe(viewLifecycleOwner, {
                series.clear()
                series.addAll(it)
            })
        }

        if (hasCreator) {
            val fragment = FilterFragment(
                Filter.CREATOR,
                filter.filterMap[Filter.CREATOR]!!
            )
            fragmentMap[Filter.CREATOR] = fragment

            childFragmentManager
                .beginTransaction()
                .replace(
                    R.id.creator_filter,
                    fragment
                ).commit()
            creatorFilter.visibility = View.VISIBLE

            filterListViewModel.creators.observe(viewLifecycleOwner, {
                creators.clear()
                creators.addAll(it)
            })
        }

        filterButton.setOnClickListener {
            text = when {
                hasName -> {
                    if (nameFilter.text.isNullOrBlank()) null
                    else nameFilter.text.toString().trim()
                }
                hasTitle -> {
                    if (titleFilter.text.isNullOrBlank()) null
                    else titleFilter.text.toString().trim()
                }
                else -> null
            }

            val filterMap = hashMapOf<Int, List<FilterServiceResponse>>()

            if (hasCharacter) {
                filterMap[Filter.CHARACTER] = characters
            }

            if (hasComic) {
                filterMap[Filter.COMIC] = comics
            }

            if (hasEvent) {
                filterMap[Filter.EVENT] = events
            }

            if (hasSeries) {
                filterMap[Filter.SERIES] = series
            }

            if (hasCreator) {
                filterMap[Filter.CREATOR] = creators
            }

            filter = Filter(text, filterMap)

            callbackListener.onDataReceived(filter)
            dismiss()
        }

        closeFilters.setOnClickListener {
            if (filter.isEmpty()) {
                callbackListener.onDataReceived(filter)
            }
            dismiss()
        }

        clearFilters.setOnClickListener {
            nameFilter.text = null
            titleFilter.text = null
            filter = Filter()

            for((_, v) in fragmentMap) {
                v.clearFilter()
            }
        }
    }
}