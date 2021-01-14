package com.example.marvelworld.util

import android.content.res.Resources
import androidx.room.TypeConverter
import com.example.marvelworld.R

class Converters {

    @TypeConverter
    fun fromResourceType(resourceType: ResourceType): Int {
        return resourceType.ordinal
    }

    @TypeConverter
    fun toResourceType(value: Int): ResourceType {
        return when (value) {
            0 -> ResourceType.CHARACTER
            1 -> ResourceType.COMIC
            2 -> ResourceType.CREATOR
            3 -> ResourceType.EVENT
            4 -> ResourceType.SERIES
            5 -> ResourceType.STORY
            else -> throw IllegalArgumentException(
                Resources.getSystem().getString(R.string.wrong_type_of_resource)
            )
        }
    }
}