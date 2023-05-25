package com.danrsy.rstoryapp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.danrsy.rstoryapp.utils.CoroutinesTestRule
import com.danrsy.rstoryapp.data.RStoryRepository
import com.danrsy.rstoryapp.data.local.room.entity.StoryEntity
import com.danrsy.rstoryapp.ui.adapter.StoryListAdapter
import com.danrsy.rstoryapp.ui.story.StoryViewModel
import com.danrsy.rstoryapp.utils.DummyData
import com.danrsy.rstoryapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyRepository: RStoryRepository

    private val dummyToken = DummyData.generateDummyToken()

    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyStories = DummyData.generateDummyListStory()
        val data: PagingData<StoryEntity> = StoryPagingSource.snapshot(dummyStories)
        val expectedQuote = MutableLiveData<PagingData<StoryEntity>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getStories(dummyToken)).thenReturn(expectedQuote.asFlow())

        val storyViewModel = StoryViewModel(storyRepository)
        val actualQuote: PagingData<StoryEntity> =
            storyViewModel.getStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Quote Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryEntity> = PagingData.from(emptyList())
        val expectedQuote = MutableLiveData<PagingData<StoryEntity>>()
        expectedQuote.value = data
        Mockito.`when`(storyRepository.getStories(dummyToken)).thenReturn(expectedQuote.asFlow())

        val storyViewModel = StoryViewModel(storyRepository)
        val actualQuote: PagingData<StoryEntity> =
            storyViewModel.getStory(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualQuote)

        assertEquals(0, differ.snapshot().size)
    }

}


private val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class StoryPagingSource : PagingSource<Int, LiveData<List<StoryEntity>>>() {
    companion object {
        fun snapshot(items: List<StoryEntity>): PagingData<StoryEntity> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryEntity>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryEntity>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }
}