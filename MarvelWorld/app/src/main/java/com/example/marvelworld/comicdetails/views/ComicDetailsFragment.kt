package com.example.marvelworld.comicdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marvelworld.R
import com.example.marvelworld.comicdetails.respository.ComicDetailsRepository
import com.example.marvelworld.comicdetails.viewmodel.ComicDetailsViewModel
import com.example.marvelworld.detailcard.models.DetailCard
import com.example.marvelworld.detailcard.views.DetailCardFragment
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListFragment
import com.example.marvelworld.util.ResourceType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class ComicDetailsFragment : Fragment() {

    private lateinit var comicDetailsViewModel: ComicDetailsViewModel
    private var comicId by Delegates.notNull<Int>()
    private val horizontalListFragmentMap = mutableMapOf<ResourceType, HorizontalListFragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comic_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        comicId = requireArguments().getInt("COMIC_ID")

        comicDetailsViewModel = ViewModelProvider(
            this,
            ComicDetailsViewModel.ComicDetailsViewModelFactory(
                ComicDetailsRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(ComicDetailsViewModel::class.java)

        comicDetailsViewModel.getComic(comicId)
            .observe(viewLifecycleOwner, { comic ->
                val card = DetailCard(
                    comic.id,
                    comic.title,
                    comic.thumbnail,
                    comic.description,
                    comic.urls,
                    ResourceType.COMIC,
                    comic.isFavorite
                )

                childFragmentManager.beginTransaction().replace(
                    R.id.detail_card,
                    DetailCardFragment(card)
                ).commit()

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

        if (horizontalListFragmentMap.isEmpty()) {
            inflateFragment(
                R.id.character_list,
                getString(R.string.characters),
                ResourceType.CHARACTER
            )
            inflateFragment(R.id.creator_list, getString(R.string.creators), ResourceType.CREATOR)
            inflateFragment(R.id.event_list, getString(R.string.events), ResourceType.EVENT)
            inflateFragment(R.id.story_list, getString(R.string.stories), ResourceType.STORY)
        }
    }

    private fun inflateFragment(layoutId: Int, title: String, type: ResourceType) {
        val horizontalListFragment =
            HorizontalListFragment(comicDetailsViewModel, title, type, comicId)

        horizontalListFragmentMap[type] = horizontalListFragment

        childFragmentManager.beginTransaction().replace(
            layoutId,
            horizontalListFragment
        ).commit()
    }
}