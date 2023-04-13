package com.danrsy.rstoryapp.ui.story

import androidx.lifecycle.ViewModel
import com.danrsy.rstoryapp.data.RStoryRepository

class StoryViewModel : ViewModel() {

    private val repository = RStoryRepository()

    suspend fun getListStory(auth: String) = repository.getStories(auth)
}