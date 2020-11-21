package com.example.marvelworld.characterlist.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelworld.R
import com.example.marvelworld.characterlist.repository.CharacterRepository
import com.example.marvelworld.characterlist.models.Character
import com.example.marvelworld.characterlist.viewmodel.CharacterViewModel

class CharacterListFragment : Fragment(), OnCharacterClickListener {
    private val characterList = mutableListOf<Character>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_characeter_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_character_list)
        val manager = GridLayoutManager(view.context, 2)
        val characterListAdapter = CharacterListAdapter(characterList, this)

        recycler.apply {
            layoutManager = manager
            adapter = characterListAdapter
        }

        val characterViewModel = ViewModelProvider(
            this,
            CharacterViewModel.CharacterViewModelFactory(CharacterRepository())
        ).get(CharacterViewModel::class.java)

        characterViewModel.getCharacters().observe(viewLifecycleOwner, Observer {
            characterList.clear()
            characterList.addAll(it)
            characterListAdapter.notifyDataSetChanged()
        })
    }

    override fun onCharacterClick(position: Int) {
        val bundle = bundleOf("CHARACTER" to characterList[position])
        findNavController().navigate(R.id.characterDetailsFragment, bundle)
    }
}