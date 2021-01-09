package com.example.marvelworld.comicdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.marvelworld.R
import com.example.marvelworld.api.models.Image
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
            .observe(viewLifecycleOwner, { comic ->
                val card = Card(
                    comic.title,
                    comic.thumbnail.getImagePath(Image.LANDSCAPE_INCREDIBLE),
                    comic.thumbnail.getImagePath(Image.FULL_SIZE),
                    comic.description,
                    comic.urls
                )
                ExpandableCardUtils.initCard(view, card, childFragmentManager)

                val publishDate = view.findViewById<TextView>(R.id.publish_date)
                val date = comic.dates.find { date ->
                    date.type == "onsaleDate"
                }?.date

                if (date != null) {
                    publishDate.text = SimpleDateFormat("MMMM dd, yyyy", Locale.ROOT)
                        .format(date)
                        .toString()
                } else {
                    val publishDateLabel = view.findViewById<TextView>(R.id.publish_date_label)
                    publishDateLabel.visibility = View.GONE
                }
            })

        comicDetailsViewModel.getComicCharacters(comicId)
            .observe(viewLifecycleOwner, {
                val characterList = view.findViewById<LinearLayout>(R.id.character_list)
                HorizontalListUtils.initHorizontalList(
                    characterList,
                    it,
                    getString(R.string.characters),
                    this
                )
            })

        comicDetailsViewModel.getComicStories(comicId)
            .observe(viewLifecycleOwner, {
                val storyList = view.findViewById<LinearLayout>(R.id.story_list)
                HorizontalListUtils.initHorizontalList(
                    storyList,
                    it,
                    getString(R.string.stories),
                    this
                )
            })

        comicDetailsViewModel.getComicEvents(comicId)
            .observe(viewLifecycleOwner, {
                val eventList = view.findViewById<LinearLayout>(R.id.event_list)
                HorizontalListUtils.initHorizontalList(
                    eventList,
                    it,
                    getString(R.string.events),
                    this
                )
            })

        comicDetailsViewModel.getComicCreators(comicId)
            .observe(viewLifecycleOwner, {
                val creatorList = view.findViewById<LinearLayout>(R.id.creator_list)
                HorizontalListUtils.initHorizontalList(
                    creatorList,
                    it,
                    getString(R.string.creators),
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