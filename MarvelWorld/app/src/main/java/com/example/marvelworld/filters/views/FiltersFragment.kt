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
import androidx.lifecycle.ViewModelProvider
import com.example.marvelworld.R
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.filters.repository.FiltersRepository
import com.example.marvelworld.filters.viewmodel.FiltersViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.MaterialAutoCompleteTextView


class FiltersFragment : Fragment() {
    private lateinit var handler: Handler
    private lateinit var characterSuggestAdapter: SuggestAdapter
    private val selectedCharacters = mutableListOf<Character>()
    private val characters = mutableListOf<Character>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filtersViewModel = ViewModelProvider(
            this,
            FiltersViewModel.FiltersViewModelFactory(FiltersRepository())
        ).get(FiltersViewModel::class.java)

        val characterFilter = view.findViewById<MaterialAutoCompleteTextView>(R.id.character_filter)
        val chipGroup = view.findViewById<ChipGroup>(R.id.character_chip_group)

        characterSuggestAdapter = SuggestAdapter(
            view.context,
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )

        characterFilter.apply {
            threshold = 4
            setAdapter(characterSuggestAdapter)
        }


        characterFilter.setOnItemClickListener { _, _, position, _ ->
            val character = characters[position]

            selectedCharacters.add(character)

            val chip = Chip(view.context)
            chip.apply {
                text = character.name
                setChipBackgroundColorResource(R.color.colorPrimary)
                isCloseIconVisible = true
                setTextColor(ContextCompat.getColor(view.context, R.color.white))
                setTextAppearance(R.style.ChipFilterTextAppearanceTheme)
                tag = selectedCharacters.indexOf(character)
            }

            chipGroup.addView(chip)

            chip.setOnCloseIconClickListener {
                selectedCharacters.removeAt(it.tag.toString().toInt())
                chipGroup.removeView(it)
            }

            characterFilter.setText("")
            characterSuggestAdapter.setData(mutableListOf())
        }

        characterFilter.addTextChangedListener(object : TextWatcher {
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
                if (characterFilter.text.isNotBlank()) {
                    filtersViewModel.getCharacters(characterFilter.text.toString())
                        .observe(viewLifecycleOwner, { list ->
                            characters.clear()
                            characters.addAll(list)
                            characterSuggestAdapter.setData(list.map { c -> c.name })
                            characterSuggestAdapter.notifyDataSetChanged()
                        })
                }
            }
            false
        }
    }

    companion object {
        private const val TRIGGER_AUTO_COMPLETE = 100
        private const val AUTO_COMPLETE_DELAY: Long = 1000
    }
}