package com.develop.home

import com.develop.common.constants.AppLanguage
import com.develop.domain.usecase.FetchArticlesUC
import com.develop.domain.usecase.FetchTopHeadersUC
import com.develop.home.ui.HomeViewModel
import com.develop.materialnews.testing.repository.TestAppSettings
import com.develop.materialnews.testing.repository.TestArticleRepository
import com.develop.materialnews.testing.repository.TestLanguageHelper
import com.develop.materialnews.testing.repository.TestTopHeaderRepository
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

class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val topHeaderRepository = TestTopHeaderRepository()
    private val appSettings = TestAppSettings()
    private val languageHelper = TestLanguageHelper(appSettings)
    private val articleRepository = TestArticleRepository()
    private val userFilterRepository = TestUserFilterRepository()
    private val fetchArticleUC = FetchArticlesUC(articleRepository, userFilterRepository)
    private val fetchTopHeadersUC = FetchTopHeadersUC(topHeaderRepository, appSettings)


    private val viewModel by lazy {
        HomeViewModel(
            topHeaderRepository,
            appSettings,
            languageHelper,
            fetchArticleUC,
            fetchTopHeadersUC
            )
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }


    @Test
    fun `change language`() = runTest{
        val state = viewModel.settingsState

        val newLng = AppLanguage.UK
        state.changeLng(newLng)

        val currentLng = state.uiState.currentLng.firstOrNull { it == newLng }

        Assert.assertNotNull(currentLng)
        Assert.assertEquals(newLng, currentLng)
    }

    @Test
    fun `change to dark theme`() = runTest{
        val state = viewModel.settingsState

        state.toggleIsDarkTheme(true)

        val isDark = state.uiState.isDarkTheme.firstOrNull { it == true }

        Assert.assertNotNull(isDark)
        Assert.assertTrue(isDark!!)
    }


    @After
    fun clear(){
        testDispatcher.cancel()
    }
}