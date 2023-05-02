package com.danrsy.rstoryapp.ui.story.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RStoryRepository

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = RStoryRepository(application)

    fun getDetailStory(auth: String, id: String) = repository.getDetailStories(auth, id).asLiveData()
}