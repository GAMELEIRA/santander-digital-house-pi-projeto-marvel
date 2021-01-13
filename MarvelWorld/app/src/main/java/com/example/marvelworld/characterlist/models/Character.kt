package com.example.marvelworld.characterlist.models

import com.example.marvelworld.api.models.*
import java.util.*

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val modified: Date,
    val resourceURI: String,
    val urls: List<Url>,
    val thumbnail: Image,
    val comics: ComicList,
    val stories: StoryList,
    val events: EventList,
    val series: SeriesList,
    var isFavorite: Boolean = false
)