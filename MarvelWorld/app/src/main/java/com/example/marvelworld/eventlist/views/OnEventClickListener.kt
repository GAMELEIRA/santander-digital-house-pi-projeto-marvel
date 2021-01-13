package com.example.marvelworld.eventlist.views

interface OnEventClickListener {
    fun onEventClick(position: Int)
    fun onEventFavoriteClick(position: Int)
}