package com.danrsy.rstoryapp.ui.story.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RStoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(repository: RStoryRepository): ViewModel() {

    private val repo = repository
    fun uploadStory(auth: String, file: MultipartBody.Part, description: RequestBody) = repo.uploadStories(auth, file, description).asLiveData()
}