package com.develop.filters

import com.develop.data.models.filters.SortBy
import com.develop.data.models.spurces.generateDummyModel
import com.develop.filters.ui.FilterViewModel
import com.develop.materialnews.testing.repository.TestSourceRepository
import com.develop.materialnews.testing.repository.TestUserFilterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class FilterViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val fakeUserFilterRepository = TestUserFilterRepository()
    private val fakeSourceRepository = TestSourceRepository()

    private val viewModel by lazy {
        FilterViewModel(fakeUserFilterRepository, fakeSourceRepository)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `change sort order and check`() = runTest {
        val testOrder = SortBy.RELEVANCY
        viewModel.state.sortByState.onSetSort(testOrder)
        val order = viewModel.state.sortByState.sortBy.firstOrNull { it == testOrder }
        Assert.assertNotNull(order)
        Assert.assertEquals(testOrder, order)
    }

    @Test
    fun `select source and check if any changes`() = runTest{
        val state = viewModel.state.sourceState
        val initSources = List(5){ generateDummyModel(it) }
        val selected = initSources.first()
        fakeSourceRepository.addSources(initSources)

        state.onSelect(selected)
        val selectedSources = state.selectedSources.firstOrNull { it.isNotEmpty() }
        Assert.assertTrue(selectedSources!!.isNotEmpty())
        Assert.assertTrue(selectedSources.size == 1)
        Assert.assertEquals(selectedSources.first(), selected.id)

        val anyChanges = viewModel.state.anyChanges.firstOrNull()
        Assert.assertTrue(anyChanges!!)
    }

    @After
    fun clear(){
        fakeUserFilterRepository.clear()
        fakeUserFilterRepository.clear()
        testDispatcher.cancel()
    }
}