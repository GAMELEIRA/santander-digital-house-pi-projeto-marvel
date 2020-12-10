package com.example.marvelworld.filters.views

import com.example.marvelworld.filters.models.Filter

interface CallbackListener {
    fun onDataReceived(filter: Filter)
}