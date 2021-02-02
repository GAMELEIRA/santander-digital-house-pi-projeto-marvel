package com.example.marvelworld.characterdetails.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.marvelworld.R
import com.example.marvelworld.characterdetails.repository.CharacterDetailsRepository
import com.example.marvelworld.characterdetails.viewmodel.CharacterDetailsViewModel
import com.example.marvelworld.detailcard.models.DetailCard
import com.example.marvelworld.detailcard.views.DetailCardFragment
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.horizontallist.HorizontalListFragment
import com.example.marvelworld.util.ResourceType
import kotlin.properties.Delegates


class CharacterDetailsFragment : Fragment() {

    private lateinit var characterDetailsViewModel: CharacterDetailsViewModel
    private var characterId by Delegates.notNull<Int>()
    private val horizontalListFragmentMap = mutableMapOf<ResourceType, HorizontalListFragment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterId = requireArguments().getInt("CHARACTER_ID")

        characterDetailsViewModel = ViewModelProvider(
            this,
            CharacterDetailsViewModel.CharacterDetailsViewModelFactory(
                CharacterDetailsRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(CharacterDetailsViewModel::class.java)

        characterDetailsViewModel.getCharacter(characterId)
            .observe(viewLifecycleOwner, { character ->
                val card = DetailCard(
                    character.id,
                    character.name,
                    character.thumbnail,
                    character.description,
                    character.urls,
                    ResourceType.CHARACTER,
                    character.isFavorite
                )

                childFragmentManager.beginTransaction().replace(
                    R.id.detail_card,
                    DetailCardFragment(card)
                ).commit()
            })

        if (horizontalListFragmentMap.isEmpty()) {
            inflateFragment(R.id.comic_list, getString(R.string.comics), ResourceType.COMIC)
            inflateFragment(R.id.event_list, getString(R.string.events), ResourceType.EVENT)
            inflateFragment(R.id.series_list, getString(R.string.series), ResourceType.SERIES)
            inflateFragment(R.id.story_list, getString(R.string.stories), ResourceType.STORY)
        }
    }

    private fun inflateFragment(layoutId: Int, title: String, type: ResourceType) {
        val horizontalListFragment =
            HorizontalListFragment(characterDetailsViewModel, title, type, characterId)

        horizontalListFragmentMap[type] = horizontalListFragment

        childFragmentManager.beginTransaction().replace(
            layoutId,
            horizontalListFragment
        ).commit()
    }
}