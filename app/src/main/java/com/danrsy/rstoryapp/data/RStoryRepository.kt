package com.danrsy.rstoryapp.data

import android.app.Application
import com.danrsy.rstoryapp.data.local.theme.ThemeDataStore
import com.danrsy.rstoryapp.data.model.login.LoginResponse
import com.danrsy.rstoryapp.data.model.register.RegisterResponse
import com.danrsy.rstoryapp.data.model.story.DetailStoryResponse
import com.danrsy.rstoryapp.data.model.story.ListStoryResponse
import com.danrsy.rstoryapp.data.model.upload.UploadResponse
import com.danrsy.rstoryapp.data.remote.ApiConfig
import com.danrsy.rstoryapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class RStoryRepository(application: Application) {

    private val dataStore: ThemeDataStore

    init {
        dataStore = ThemeDataStore.getInstance(application)
    }

    fun registerUser(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<RegisterResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = ApiConfig.getApiService().register(name, email, password)
            emit(Resource.Success(response))
        } catch (exception: Exception) {
            val e = (exception as? HttpException)?.response()?.errorBody()?.string()
            emit(Resource.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    fun loginUser(
        email: String,
        password: String
    ): Flow<Resource<LoginResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = ApiConfig.getApiService().login(email, password)
            emit(Resource.Success(response))
        } catch (exception: Exception) {
            val e = (exception as? HttpException)?.response()?.errorBody()?.string()
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getStories(auth: String): Flow<Resource<ListStoryResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = ApiConfig.getApiService().getStories(auth)
            emit(Resource.Success(response))
        } catch (exception: Exception) {
            val e = (exception as? HttpException)?.response()?.errorBody()?.string()
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun getDetailStories(auth: String, id: String): Flow<Resource<DetailStoryResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = ApiConfig.getApiService().getDetailStories(auth, id)
            emit(Resource.Success(response))
        } catch (exception: Exception) {
            val e = (exception as? HttpException)?.response()?.errorBody()?.string()
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    fun uploadStories(
        auth: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Flow<Resource<UploadResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = ApiConfig.getApiService().uploadStory(auth, file, description)
            emit(Resource.Success(response))
        } catch (exception: Exception) {
            val e = (exception as? HttpException)?.response()?.errorBody()?.string()
            emit(Resource.Error(e.toString()))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveThemeSetting(theme: Int) = dataStore.saveThemeSetting(theme)

    fun getThemeSetting() = dataStore.getThemeSetting()
}