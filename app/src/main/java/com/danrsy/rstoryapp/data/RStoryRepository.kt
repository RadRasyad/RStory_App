package com.danrsy.rstoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.danrsy.rstoryapp.data.model.login.LoginResponse
import com.danrsy.rstoryapp.data.model.register.RegisterResponse
import com.danrsy.rstoryapp.data.model.story.DetailStoryResponse
import com.danrsy.rstoryapp.data.model.story.ListStoryResponse
import com.danrsy.rstoryapp.data.remote.ApiConfig
import com.danrsy.rstoryapp.utils.Resource
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RStoryRepository {

    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<Resource<RegisterResponse>> {
        val result = MutableLiveData<Resource<RegisterResponse>>()

        result.value = Resource.Loading()

        ApiConfig.getApiService().register(name, email, password)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        result.value = Resource.Success(response.body())
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    result.value = Resource.Error(t.message)
                }
            })
        return result
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): LiveData<Resource<LoginResponse>> {
        val result = MutableLiveData<Resource<LoginResponse>>()

        result.value = Resource.Loading()

        ApiConfig.getApiService().login(email, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        result.value = Resource.Success(response.body())
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    result.value = Resource.Error(t.message)
                }
            })
        return result
    }

    suspend fun getStories(auth: String): LiveData<Resource<ListStoryResponse>> {
        val result = MutableLiveData<Resource<ListStoryResponse>>()

        result.value = Resource.Loading()
        ApiConfig.getApiService().getStories(auth)
            .enqueue(object : Callback<ListStoryResponse> {
                override fun onResponse(
                    call: Call<ListStoryResponse>,
                    response: Response<ListStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        result.value = Resource.Success(response.body())
                    }
                }

                override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                    result.value = Resource.Error(t.message)
                }

            })

        return result
    }

    suspend fun getDetailStories(auth: String, id: String): LiveData<Resource<DetailStoryResponse>>  {
        val result = MutableLiveData<Resource<DetailStoryResponse>>()

        result.value = Resource.Loading()
        ApiConfig.getApiService().getDetailStories(auth, id)
            .enqueue(object : Callback<DetailStoryResponse> {
                override fun onResponse(
                    call: Call<DetailStoryResponse>,
                    response: Response<DetailStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        result.value = Resource.Success(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    result.value = Resource.Error(t.message)
                }

            })

        return result
    }

    suspend fun uploadStories(auth: String, file: MultipartBody.Part, description: String) {

    }
}