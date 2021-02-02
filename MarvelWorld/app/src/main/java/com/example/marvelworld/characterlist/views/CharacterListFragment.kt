package com.example.marvelworld.characterlist.views

import android.os.Bundle
import android.view.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.characterlist.repository.CharacterRepository
import com.example.marvelworld.characterlist.viewmodel.CharacterViewModel
import com.example.marvelworld.favorite.db.AppDatabase
import com.example.marvelworld.favorite.respository.FavoriteRepository
import com.example.marvelworld.filters.models.Filter
import com.example.marvelworld.filters.views.CallbackListener
import com.example.marvelworld.filters.views.FilterListFragment

class CharacterListFragment(
    private val onlyFavorites: Boolean = false
) : Fragment(),
    OnCharacterClickListener,
    CallbackListener {

    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var characterListAdapter: CharacterListAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var filterIcon: MenuItem
    private var characterList = mutableListOf<Character>()
    private var filter = Filter()
    private var loading = false
    private var onPause = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onPause() {
        super.onPause()

        onPause = true
    }

    override fun onResume() {
        super.onResume()
        if (onPause) {
            updateCharacter()
            onPause = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!onlyFavorites) setHasOptionsMenu(true)

        recycler = view.findViewById(R.id.recycler_character_list)

        val manager = GridLayoutManager(view.context, 2)
        characterListAdapter = CharacterListAdapter(characterList, this)

        recycler.apply {
            layoutManager = manager
            adapter = characterListAdapter
        }

        characterViewModel = ViewModelProvider(
            this,
            CharacterViewModel.CharacterViewModelFactory(
                CharacterRepository(),
                FavoriteRepository(AppDatabase.getDatabase(view.context).favoriteDao())
            )
        ).get(CharacterViewModel::class.java)

        if (characterList.isEmpty()) getCharacters()

        initInfiniteScroll()
    }

    private fun updateCharacter() {
        if (!onlyFavorites) {
            characterViewModel.updateCharacters(characterList)
                .observe(viewLifecycleOwner, {
                    characterListAdapter.notifyDataSetChanged()
                })
        } else {
            characterViewModel.updateFavoriteCharacters(characterList)
                .observe(viewLifecycleOwner, {
                    characterList.removeAll(it)
                    characterListAdapter.notifyDataSetChanged()
                })
        }
    }

    private fun getCharacters() {
        loading = true
        if (onlyFavorites) {
            characterViewModel.getFavoriteCharacters().observe(viewLifecycleOwner, {
                characterList.addAll(it)
                characterListAdapter.notifyDataSetChanged()
                loading = false
            })
        } else {
            characterViewModel.getCharacters().observe(viewLifecycleOwner, {
                characterList.addAll(it)
                characterListAdapter.notifyDataSetChanged()
                loading = false
            })
        }
    }

    private fun initInfiniteScroll() {
        recycler.run {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val target = recyclerView.layoutManager as GridLayoutManager
                    val totalItemCount = target.itemCount
                    val lastVisible = target.findLastVisibleItemPosition()

                    val total = if (!onlyFavorites) {
                        characterViewModel.total
                    } else {
                        characterViewModel.totalFavorite
                    }

                    if (totalItemCount - lastVisible < 10 && totalItemCount < total && !loading) {
                        getCharacters()
                    }
                }
            })
        }
    }

    override fun onCharacterClick(position: Int) {
        val bundle = bundleOf("CHARACTER_ID" to characterList[position].id)
        findNavController().navigate(R.id.characterDetailsFragment, bundle)
    }

    override fun onCharacterFavoriteClick(position: Int) {
        characterViewModel.isFavorite(characterList[position].id)
            .observe(viewLifecycleOwner, { isFavorite ->
                if (isFavorite) {
                    characterViewModel.removeFavorite(characterList[position].id)
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                characterList[position].isFavorite = false
                                if (onlyFavorites) {
                                    characterList.removeAt(position)
                                    characterListAdapter.notifyDataSetChanged()
                                } else {
                                    characterListAdapter.notifyItemChanged(position)
                                }
                            }
                        })
                } else {
                    val character = characterList[position]
                    characterViewModel.addFavorite(
                        character.id,
                        character.name,
                        character.thumbnail.path,
                        character.thumbnail.extension
                    )
                        .observe(viewLifecycleOwner, {
                            if (it) {
                                characterList[position].isFavorite = true
                                characterListAdapter.notifyItemChanged(position)
                            }
                        })
                }

                characterList[position].isFavorite = !characterList[position].isFavorite
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        filterIcon = menu.findItem(R.id.filtersFragment)

        updateFilterIcon()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            filterIcon.itemId -> {
                FilterListFragment(
                    hasName = true,
                    hasTitle = false,
                    hasCharacter = false,
                    hasComic = true,
                    hasEvent = true,
                    hasSeries = true,
                    hasCreator = false,
                    callbackListener = this,
                    filter = filter
                ).show(childFragmentManager, "add_filters")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDataReceived(filter: Filter) {
        this.filter = filter
        updateFilterIcon()
        characterViewModel.applyFilter(this.filter)
        characterList.clear()
        getCharacters()
    }

    private fun updateFilterIcon() {
        if (filter.isEmpty()) {
            filterIcon.icon =
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_filter_alt_24px,
                    null
                )
        } else {
            filterIcon.icon =
                ResourcesCompat.getDrawable(
                    requireContext().resources,
                    R.drawable.ic_filter_filled_alt_24px,
                    null
                )
        }
    }
}