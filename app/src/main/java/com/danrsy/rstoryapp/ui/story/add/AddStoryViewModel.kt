package com.danrsy.rstoryapp.ui.story.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.danrsy.rstoryapp.data.RStoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = RStoryRepository(application)

    fun uploadStory(auth: String, file: MultipartBody.Part, description: RequestBody) = repository.uploadStories(auth, file, description).asLiveData()
}