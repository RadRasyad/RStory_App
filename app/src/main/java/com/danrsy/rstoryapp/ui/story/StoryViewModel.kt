package com.danrsy.rstoryapp.ui.story

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RStoryRepository

class StoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RStoryRepository(application)

    fun getListStory(auth: String) = repository.getStories(auth).asLiveData()
}