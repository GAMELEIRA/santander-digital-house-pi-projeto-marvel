package com.example.marvelworld.comicdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marvelworld.R
import com.example.marvelworld.comicdetails.respository.ComicDetailsRepository
import com.example.marvelworld.comicdetails.viewmodel.ComicDetailsViewModel
import com.example.marvelworld.reusablecomponents.expandablecard.Card
import com.example.marvelworld.reusablecomponents.expandablecard.ExpandableCardUtils
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListItem
import com.example.marvelworld.reusablecomponents.horizontallist.HorizontalListUtils
import com.example.marvelworld.reusablecomponents.horizontallist.OnHorizontalListItemClickListener
import java.text.SimpleDateFormat
import java.util.*

class ComicDetailsFragment : Fragment(), OnHorizontalListItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val comicId = requireArguments().getInt("COMIC_ID")

        val comicDetailsViewModel = ViewModelProvider(
            this,
            ComicDetailsViewModel.ComicDetailsViewModelFactory(ComicDetailsRepository())
        ).get(ComicDetailsViewModel::class.java)

        comicDetailsViewModel.getComic(comicId)
            .observe(viewLifecycleOwner, Observer { comic ->
                val card = Card(
                    comic.title,
                    comic.thumbnail.getImagePath(),
                    comic.description,
                    comic.urls
                )
                ExpandableCardUtils.initCard(view, card, childFragmentManager)

                val publishDate = view.findViewById<TextView>(R.id.publish_date)
                val date = comic.dates.find { date ->
                    date.type == "onsaleDate"
                }?.date

                if (date != null) {
                    publishDate.text = SimpleDateFormat("MMMMM dd, yyyy", Locale.US)
                        .format(date)
                        .toString()
                } else {
                    val publishDateLabel = view.findViewById<TextView>(R.id.publish_date_label)
                    publishDateLabel.visibility = View.GONE
                }
            })

        comicDetailsViewModel.getComicCharacters(comicId)
            .observe(viewLifecycleOwner, Observer {
                val characterList = view.findViewById<LinearLayout>(R.id.character_list)
                HorizontalListUtils.initHorizontalList(
                    characterList,
                    it,
                    "Characters:",
                    this
                )
            })

        comicDetailsViewModel.getComicStories(comicId)
            .observe(viewLifecycleOwner, Observer {
                val storyList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    storyList,
                    it,
                    "Stories:",
                    this
                )
            })

        comicDetailsViewModel.getComicEvents(comicId)
            .observe(viewLifecycleOwner, Observer {
                val eventList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    eventList,
                    it,
                    "Events:",
                    this
                )
            })

        comicDetailsViewModel.getComicCreators(comicId)
            .observe(viewLifecycleOwner, Observer {
                val creatorList = view.findViewById<LinearLayout>(R.id.creator_list)
                HorizontalListUtils.initHorizontalList(
                    creatorList,
                    it,
                    "Creators:",
                    this
                )
            })
    }

    override fun onHorizontalListItemClick(item: HorizontalListItem) {
        val bundle = bundleOf()
        when (item.type) {
            HorizontalListUtils.CHARACTER -> {
                bundle.putInt("CHARACTER_ID", item.id)
                findNavController().navigate(R.id.characterDetailsFragment, bundle)
            }
            HorizontalListUtils.EVENT -> {
                bundle.putInt("EVENT_ID", item.id)
                findNavController().navigate(R.id.eventDetailsFragment, bundle)
            }
            HorizontalListUtils.STORY -> {
                bundle.putInt("STORY_ID", item.id)
                findNavController().navigate(R.id.storyDetailsFragment, bundle)
            }
            HorizontalListUtils.CREATOR -> {
                bundle.putInt("CREATOR_ID", item.id)
                findNavController().navigate(R.id.creatorDetailsFragment, bundle)
            }
        }
    }
}