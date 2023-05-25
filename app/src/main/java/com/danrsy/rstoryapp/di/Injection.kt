package com.danrsy.rstoryapp.di

import android.content.Context
import com.danrsy.rstoryapp.data.RStoryRepository
import com.danrsy.rstoryapp.data.local.room.database.StoryDatabase
import com.danrsy.rstoryapp.data.remote.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): RStoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return RStoryRepository(database, apiService)
    }
}