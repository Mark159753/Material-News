package com.develop.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.develop.data.models.filters.SortBy
import com.develop.data.repositories.filter.UserFilterRepository
import com.develop.data.repositories.filter.UserFilterRepositoryImpl
import com.develop.local.preferences.filter.UserFilterSerializer
import com.develop.local.proto.UserFilterPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.io.File

@RunWith(RobolectricTestRunner::class)
class UserFilterRepositoryTest {

    companion object{
        private const val TEST_DATA_STORE_FILE_NAME = "test_user_filters"
    }

    private lateinit var dataStore:DataStore<UserFilterPreferences>
    private lateinit var repository: UserFilterRepository

    private val context:Context by lazy {
        RuntimeEnvironment.getApplication()
    }

    @Before
    fun setup() {
        dataStore = DataStoreFactory.create(
            produceFile = {
                context.dataStoreFile(TEST_DATA_STORE_FILE_NAME)
            },
            serializer = UserFilterSerializer
        )
        repository = UserFilterRepositoryImpl(dataStore)
    }

    @Test
    fun `save and check sort order`() = runTest {
        repository.updatePreferences {
            setSortOrder(SortBy.POPULARITY)
        }
        val order = repository.filters.firstOrNull()
        Assert.assertNotNull(order)
        Assert.assertEquals(SortBy.POPULARITY, order!!.sortBy)
    }

    @Test
    fun `save and check sources`() = runTest {
        val sources = setOf("BBC", "CBS", "ABC")

        repository.updatePreferences {
            setSources(sources)
        }
        val filters = repository.filters.firstOrNull()
        Assert.assertNotNull(filters)
        Assert.assertEquals(sources, filters!!.sources)
    }

    @Test
    fun `save and check filters`() = runTest {
        val sources = setOf("BBC", "CBS", "ABC")
        val sortOrder = SortBy.PUBLISHED_AT

        repository.updatePreferences {
            setSources(sources)
            setSortOrder(sortOrder)
        }
        val filters = repository.filters.firstOrNull()
        Assert.assertNotNull(filters)
        Assert.assertEquals(sources, filters!!.sources)
        Assert.assertEquals(sortOrder, filters.sortBy)
    }

    @After
    fun cleanup() {
        File(context.filesDir, "datastore").deleteRecursively()
    }
}