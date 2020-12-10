package com.example.marvelworld.filters.views

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.marvelworld.R
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.repository.FilterRepository
import com.example.marvelworld.filters.models.FilterServiceResponse
import com.example.marvelworld.filters.viewmodel.FilterListViewModel
import com.example.marvelworld.filters.viewmodel.FilterViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout

class FilterFragment(
    private val suggestType: Int,
    selectedItems: List<FilterServiceResponse>
) : Fragment() {
    private lateinit var _view: View
    private lateinit var handler: Handler
    private lateinit var suggestAdapter: SuggestAdapter
    private val suggestions = mutableListOf<FilterServiceResponse>()
    private val selectedItems = mutableListOf<FilterServiceResponse>()

    init {
        this.selectedItems.addAll(selectedItems)
    }

    private lateinit var filterViewModel: FilterViewModel

    private lateinit var filter: MaterialAutoCompleteTextView
    private lateinit var filterLayout: TextInputLayout
    private lateinit var chipGroup: ChipGroup

    private val filterListViewModel: FilterListViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _view = view

        filter = view.findViewById(R.id.filter)
        filterLayout = view.findViewById(R.id.filter_layout)
        chipGroup = view.findViewById(R.id.chip_group)

        filterLayout.hint = when (suggestType) {
            Filter.CHARACTER -> "Character"
            Filter.COMIC -> "Comic"
            Filter.EVENT -> "Event"
            Filter.SERIES -> "Series"
            Filter.CREATOR -> "Creator"
            else -> ""
        }

        filterViewModel = ViewModelProvider(
            this,
            FilterViewModel.FilterViewModelFactory(FilterRepository())
        ).get(FilterViewModel::class.java)

        suggestAdapter = SuggestAdapter(
            view.context,
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )

        filter.apply {
            threshold = 4
            setAdapter(suggestAdapter)
        }


        filter.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = suggestions[position]

            selectedItems.add(selectedItem)
            filterListViewModel.setSelectedItems(selectedItems, suggestType)

            createChip(selectedItem)

            filter.setText("")
            suggestAdapter.setData(mutableListOf())
        }

        filter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE)
                handler.sendEmptyMessageDelayed(
                    TRIGGER_AUTO_COMPLETE,
                    AUTO_COMPLETE_DELAY
                )
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        handler = Handler(Looper.myLooper()!!) { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (filter.text.isNotBlank()) {
                    filterViewModel.getSuggestions(suggestType, filter.text.toString())
                        .observe(viewLifecycleOwner, { list ->
                            suggestions.clear()
                            suggestions.addAll(list)
                            suggestAdapter.setData(list.map { c -> c.text })
                            suggestAdapter.notifyDataSetChanged()
                        })
                }
            }
            false
        }

        initFilter()
    }

    private fun createChip(
        selectedItem: FilterServiceResponse
    ) {
        val chip = Chip(_view.context)
        chip.apply {
            text = selectedItem.text
            setChipBackgroundColorResource(R.color.colorPrimary)
            isCloseIconVisible = true
            setTextColor(ContextCompat.getColor(_view.context, R.color.white))
            setTextAppearance(R.style.ChipFilterTextAppearanceTheme)
            tag = selectedItems.indexOf(selectedItem)
        }

        chipGroup.addView(chip)

        chip.setOnCloseIconClickListener {
            selectedItems.removeAt(it.tag.toString().toInt())
            filterListViewModel.setSelectedItems(selectedItems, suggestType)
            chipGroup.removeView(it)
        }
    }

    private fun initFilter() {
        filterListViewModel.setSelectedItems(selectedItems, suggestType)
        selectedItems.forEach { createChip(it) }
    }

    companion object {
        private const val TRIGGER_AUTO_COMPLETE = 100
        private const val AUTO_COMPLETE_DELAY: Long = 1000
    }

    fun clearFilter() {
        selectedItems.clear()
        filterListViewModel.setSelectedItems(selectedItems, suggestType)
        chipGroup.removeAllViews()
    }
}