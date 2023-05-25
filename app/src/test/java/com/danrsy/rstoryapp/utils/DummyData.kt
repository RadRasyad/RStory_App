package com.danrsy.rstoryapp.utils

import com.danrsy.rstoryapp.data.local.room.entity.StoryEntity
import com.danrsy.rstoryapp.data.model.login.LoginResponse
import com.danrsy.rstoryapp.data.model.login.LoginResult
import com.danrsy.rstoryapp.data.model.register.RegisterResponse
import com.danrsy.rstoryapp.data.model.story.ListStoryResponse
import com.danrsy.rstoryapp.data.model.story.StoryResponse
import com.danrsy.rstoryapp.data.model.upload.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object DummyData {
    fun generateDummyStoriesResponse(): ListStoryResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<StoryResponse>()

        for (i in 0 until 10) {
            val story = StoryResponse(
                id = "story-5kc3ADictmrTAiKd",
                name = "maulebaran",
                description = "dgfdg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1683508399383_6Y1WQKAy.jpg",
                createAt ="2023-05-08T01:13:19.384Z",
                lon = -16.002,
                lat = -10.212
            )
            listStory.add(story)
        }
        return ListStoryResponse(error, message, listStory)
    }

    fun generateDummyListStory(): List<StoryEntity> {
        val items = arrayListOf<StoryEntity>()

        for (i in 0 until 10) {
            val story = StoryEntity(
                id = "story-5kc3ADictmrTAiKd",
                name = "maulebaran",
                description = "dgfdg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1683508399383_6Y1WQKAy.jpg",
                createAt ="2023-05-08T01:13:19.384Z",
                lon = -16.002,
                lat = -10.212
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyToken() : String {
        return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLW9LWUxOTHgwc2g5UTQxYVYiLCJpYXQiOjE2ODMyNTYyNDB9.t65TRPkTUIOUPae52MscAljr9w88qdYNs0eh1aw8GSM"
    }
}