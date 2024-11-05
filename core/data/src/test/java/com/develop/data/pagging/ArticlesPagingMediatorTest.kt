package com.develop.data.pagging

import androidx.datastore.core.IOException
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import com.develop.data.dummy.createFakeTopHeaderResponse
import com.develop.data.models.filters.FilterPreferencesModel
import com.develop.data.paging.articles.ArticlesPagingMediator
import com.develop.local.LocalDb
import com.develop.local.entities.ArticleEntity
import com.develop.network.ApiService
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.AfterClass
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import retrofit2.Response

@ExperimentalPagingApi
@RunWith(RobolectricTestRunner::class)
class ArticlesPagingMediatorTest {

    companion object{

        private val mockDb by lazy {
            val context = RuntimeEnvironment.getApplication()
            Room.inMemoryDatabaseBuilder(
                context,
                LocalDb::class.java,
            )
                .allowMainThreadQueries()
                .build()
        }

        private val service by lazy { mock(ApiService::class.java) }

        @AfterClass
        @Throws(IOException::class)
        @JvmStatic
        fun closeDb(){
            mockDb.close()
        }
    }


    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val fakeResponse = createFakeTopHeaderResponse(5)
        Mockito.`when`(
            service.getEverything(
                page = any(),
                sortBy = any(),
                sources = any(),
                pageSize = any(),
                to = any(),
                domains = any(),
                from = any(),
                q = any(),
            )
        ).thenReturn(Response.success(fakeResponse))

        val remoteMediator = ArticlesPagingMediator(
            mockDb,
            apiService = service,
            filters = FilterPreferencesModel()
        )

        val pagingState = PagingState<Int, ArticleEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        Assert.assertTrue(
            result is RemoteMediator.MediatorResult.Success
        )
        Assert.assertFalse(
            (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached
        )
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runTest {
        val fakeResponse = createFakeTopHeaderResponse(0)
        Mockito.`when`(
            service.getEverything(
                page = any(),
                sortBy = any(),
                sources = any(),
                pageSize = any(),
                to = any(),
                domains = any(),
                from = any(),
                q = any(),
            )
        ).thenReturn(Response.success(fakeResponse))


        val remoteMediator = ArticlesPagingMediator(
            mockDb,
            apiService = service,
            filters = FilterPreferencesModel()
        )
        val pagingState = PagingState<Int, ArticleEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue (result is RemoteMediator.MediatorResult.Success)
        assertTrue ((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runTest {
        Mockito.`when`(
            service.getEverything(
                page = any(),
                sortBy = any(),
                sources = any(),
                pageSize = any(),
                to = any(),
                domains = any(),
                from = any(),
                q = any(),
            )
        ).thenReturn(Response.error(404, ResponseBody.Companion.create("text/json".toMediaType(), content = "Not Auth")))
        val remoteMediator = ArticlesPagingMediator(
            mockDb,
            apiService = service,
            filters = FilterPreferencesModel()
        )
        val pagingState = PagingState<Int, ArticleEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Error)
    }

    @After
    fun clearDb() {
        mockDb.clearAllTables()
    }

}