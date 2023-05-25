package com.danrsy.rstoryapp.ui.story.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RStoryRepository

class DetailViewModel(repository: RStoryRepository): ViewModel() {

    private val repo = repository

    fun getDetailStory(auth: String, id: String) = repo.getDetailStories(auth, id).asLiveData()
}