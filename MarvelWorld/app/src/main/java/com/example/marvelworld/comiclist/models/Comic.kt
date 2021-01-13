package com.example.marvelworld.comiclist.models

import com.example.marvelworld.api.models.*
import java.util.*

data class Comic(
    val id: Int,
    val title: String,
    val description: String?,
    val modified: Date,
    val resourceURI: String,
    val urls: List<Url>,
    val series: SeriesSummary,
    val dates: List<ComicDate>,
    val thumbnail: Image,
    val creators: CreatorList,
    val characters: CharacterList,
    val stories: StoryList,
    val events: EventList,
    var isFavorite: Boolean = false
)