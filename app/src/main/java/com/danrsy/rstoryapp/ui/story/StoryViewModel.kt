package com.danrsy.rstoryapp.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.danrsy.rstoryapp.data.RStoryRepository

class StoryViewModel(repository: RStoryRepository): ViewModel() {

    private val repo = repository

    fun getStory(auth: String) = repo.getStories(auth).asLiveData().cachedIn(viewModelScope)
}