package com.danrsy.rstoryapp.ui.story.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RStoryRepository

class MapsViewModel(repository: RStoryRepository): ViewModel() {

    private val repo = repository

    fun getStoryLocation(auth: String) = repo.getStoriesWithLocation(auth).asLiveData()
}