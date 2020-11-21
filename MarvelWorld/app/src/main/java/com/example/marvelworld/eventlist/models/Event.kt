package com.example.marvelworld.eventlist.models

import com.example.marvelworld.api.models.*
import java.util.*

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val resourceURI: String,
    val urls: List<Url>,
    val modified: Date,
    val thumbnail: Image,
    val stories: StoryList,
    val series: SeriesList,
    val characters: CharacterList,
    val creators: CreatorList,
    val next: EventSummary,
    val previous: EventSummary
)