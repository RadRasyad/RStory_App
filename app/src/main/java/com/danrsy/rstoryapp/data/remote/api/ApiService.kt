package com.danrsy.rstoryapp.data.remote.api

import com.danrsy.rstoryapp.data.model.login.LoginResponse
import com.danrsy.rstoryapp.data.model.register.RegisterResponse
import com.danrsy.rstoryapp.data.model.story.DetailStoryResponse
import com.danrsy.rstoryapp.data.model.story.ListStoryResponse
import com.danrsy.rstoryapp.data.model.upload.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : RegisterResponse

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") auth: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("location") location: Int? = null
    ): ListStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Header("Authorization") auth: String,
        @Path("id") id : String
    ) : DetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : UploadResponse

}