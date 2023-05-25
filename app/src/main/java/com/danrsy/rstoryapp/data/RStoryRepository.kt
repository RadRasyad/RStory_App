package com.danrsy.rstoryapp.data

import androidx.paging.*
import com.danrsy.rstoryapp.data.local.room.database.StoryDatabase
import com.danrsy.rstoryapp.data.local.room.entity.StoryEntity
import com.danrsy.rstoryapp.data.model.story.DetailStoryResponse
import com.danrsy.rstoryapp.data.model.story.ListStoryResponse
import com.danrsy.rstoryapp.data.model.upload.UploadResponse
import com.danrsy.rstoryapp.data.remote.StoryRemoteMediator
import com.danrsy.rstoryapp.data.remote.api.ApiConfig
import com.danrsy.rstoryapp.data.remote.api.ApiService
import com.danrsy.rstoryapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class RStoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
) {

    fun getStories(auth: String): Flow<PagingData<StoryEntity>> {

        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryRemoteMediator(
                storyDatabase,
                apiService,
                auth
            ),
            pagingSourceFactory = {
                storyDatabase.storyDao().getStories()
            }
        ).flow
    }

    fun getStoriesWithLocation(auth: String): Flow<Resource<ListStoryResponse>> = flow {
        try {
            emit(Resource.Loading())
            val response = ApiConfig.getApiService().getStories(auth = auth, location = 1)
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

}