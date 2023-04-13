package com.danrsy.rstoryapp.data.remote

import com.danrsy.rstoryapp.data.model.login.LoginResponse
import com.danrsy.rstoryapp.data.model.register.RegisterResponse
import com.danrsy.rstoryapp.data.model.story.DetailStoryResponse
import com.danrsy.rstoryapp.data.model.story.ListStoryResponse
import com.danrsy.rstoryapp.data.model.upload.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>

    @POST("login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") auth: String
    ) : Call<ListStoryResponse>

    @GET("stories/{id}")
    suspend fun getDetailStories(
        @Header("Authorization") auth: String,
        @Path("id") id : String
    ) : Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ) : Call<UploadResponse>


}