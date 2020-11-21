package com.example.marvelworld.characterdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.marvelworld.R
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.reusablecomponents.ExpandableCardUtils
import com.example.marvelworld.reusablecomponents.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.OnHorizontalListItemClickListener


class CharacterDetailsFragment : Fragment(), OnHorizontalListItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val character = arguments?.get("CHARACTER") as Character

        ExpandableCardUtils.initCard(view, character)

        val comicSummaryList = character.comics.items
        val comicList = view.findViewById<LinearLayout>(R.id.comic_list)
        HorizontalListUtils.initHorizontalList(
            comicList,
            comicSummaryList,
            "Comics:",
            this
        )

        val storySummaryList = character.stories.items
        val storyList = view.findViewById<LinearLayout>(R.id.story_list)
        HorizontalListUtils.initHorizontalList(
            storyList,
            storySummaryList,
            "Stories:",
            this
        )

        val eventSummaryList = character.events.items
        val eventList = view.findViewById<LinearLayout>(R.id.event_list)
        HorizontalListUtils.initHorizontalList(
            eventList,
            eventSummaryList,
            "Events:",
            this
        )

        val seriesSummaryList = character.series.items
        val seriesList = view.findViewById<LinearLayout>(R.id.series_list)
        HorizontalListUtils.initHorizontalList(
            seriesList,
            seriesSummaryList,
            "Series:",
            this
        )
    }

    override fun onHorizontalListItemClick(position: Int) {
        Toast.makeText(this.context, "cliquei", Toast.LENGTH_SHORT).show()
    }
}