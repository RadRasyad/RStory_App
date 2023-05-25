package com.danrsy.rstoryapp.data

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.danrsy.rStoryEntityapp.utils.PagedTestDataSource
import com.danrsy.rstoryapp.data.local.room.database.StoryDatabase
import com.danrsy.rstoryapp.data.remote.api.ApiService
import com.danrsy.rstoryapp.ui.adapter.StoryListAdapter
import com.danrsy.rstoryapp.utils.CoroutinesTestRule
import com.danrsy.rstoryapp.utils.DummyData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RStoryRepositoryTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyDatabase: StoryDatabase

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var storyRepositoryMock: RStoryRepository

    private lateinit var storyRepository: RStoryRepository

    private val dummyToken = DummyData.generateDummyToken()
    private val dummyStoriesResponse = DummyData.generateDummyStoriesResponse()

    @Before
    fun setup() {
        storyRepository = RStoryRepository(storyDatabase, apiService)
    }


    @Test
    fun `Get story with paging - successfully`() = runTest {
        val dummyStories = DummyData.generateDummyListStory()
        val data = PagedTestDataSource.snapshot(dummyStories)

        val expectedResult = flowOf(data)

        Mockito.`when`(storyRepositoryMock.getStories(dummyToken)).thenReturn(expectedResult)

        storyRepositoryMock.getStories(dummyToken).collect { result ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(result)
            assertNotNull(differ.snapshot())
            assertEquals(
                dummyStoriesResponse.listStory.size,
                differ.snapshot().size
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Get story with paging - return empty`() = runTest {
        val data = PagedTestDataSource.snapshot(emptyList())

        val expectedResult = flowOf(data)

        Mockito.`when`(storyRepositoryMock.getStories(dummyToken)).thenReturn(expectedResult)

        storyRepositoryMock.getStories(dummyToken).collect { result ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(result)
            assertNotNull(differ.snapshot())
            assertEquals(
                0,
                differ.snapshot().size
            )
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }

}