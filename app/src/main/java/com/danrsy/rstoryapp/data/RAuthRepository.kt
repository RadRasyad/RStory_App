package com.danrsy.rstoryapp.data

import android.app.Application
import com.danrsy.rstoryapp.data.local.theme.ThemeDataStore
import com.danrsy.rstoryapp.data.model.login.LoginResponse
import com.danrsy.rstoryapp.data.model.register.RegisterResponse
import com.danrsy.rstoryapp.data.remote.api.ApiConfig
import com.danrsy.rstoryapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class RAuthRepository(application: Application) {

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

    suspend fun saveThemeSetting(theme: Int) = dataStore.saveThemeSetting(theme)

    fun getThemeSetting() = dataStore.getThemeSetting()
}