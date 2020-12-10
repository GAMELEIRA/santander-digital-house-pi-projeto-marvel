package com.example.marvelworld.filters.models

import com.google.gson.annotations.SerializedName

data class FilterServiceResponse(
    @SerializedName(value = "name", alternate = ["title", "fullName"])
    var text: String,
    val id: Int
)

