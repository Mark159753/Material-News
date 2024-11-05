package com.develop.home

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.develop.common.constants.AppLanguage
import com.develop.data.models.articles.generateFakeArticleModel
import com.develop.data.paging.createTestPagingData
import com.develop.home.ui.HomeScreen
import com.develop.home.ui.states.ArticlesState
import com.develop.home.ui.states.SettingsUiState
import com.develop.ui.R
import com.develop.ui.theme.MaterialNewsTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun click_on_top_header_and_nav_to_web_view() {
        val topHeadersList = List(5){ generateFakeArticleModel(it) }
        var isClicked = false

        composeTestRule.setContent {
            MaterialNewsTheme {
                HomeScreen(
                    articlesSate = ArticlesState(
                        topHeaderState = MutableStateFlow(topHeadersList)
                    ),
                    onNavToWebView = { isClicked = topHeadersList.first().url == it }
                )
            }
        }

        composeTestRule
            .onNodeWithTag(topHeadersList.first().url)
            .assertExists()
            .performClick()

        Assert.assertTrue(isClicked)
    }

    @Test
    fun click_article_and_nav_to_web_view() {
        val list = List(5){ generateFakeArticleModel(it) }
        val pagingArticles = list.createTestPagingData()
        var isClicked = false

        composeTestRule.setContent {
            MaterialNewsTheme {
                HomeScreen(
                    articlesSate = ArticlesState(
                        articlesPaging = pagingArticles,
                    ),
                    onNavToWebView = { isClicked = list.first().url == it }
                )
            }
        }

        composeTestRule
            .onNodeWithTag("news_item_${list.first().url}")
            .assertExists()
            .performClick()

        Assert.assertTrue(isClicked)
    }

    @Test
    fun toggle_dark_theme() {
        var isClicked = false

        composeTestRule.setContent {
            MaterialNewsTheme {
                HomeScreen(
                    settingsUiState = SettingsUiState(
                        isDarkTheme = MutableStateFlow(false)
                    ),
                    onToggleDarkTheme = { if (it) isClicked = true }
                )
            }
        }

        composeTestRule
            .onNodeWithTag(composeTestRule.activity.resources.getString(R.string.home_screen_dark_theme_bts_title))
            .assertExists()
            .performClick()

        Assert.assertTrue(isClicked)
    }

    @Test
    fun change_app_language() {
        val currentLng = AppLanguage.EN

        val desireLng = AppLanguage.UK
        var isLngChanged = false

        composeTestRule.setContent {
            MaterialNewsTheme {
                HomeScreen(
                    settingsUiState = SettingsUiState(
                        currentLng = MutableStateFlow(currentLng)
                    ),
                    onChangeLng = { isLngChanged = it == desireLng }
                )
            }
        }

        composeTestRule
            .onNodeWithTag(composeTestRule.activity.resources.getString(R.string.home_screen_language_bts_title))
            .assertExists()
            .performClick()

        composeTestRule
            .onNodeWithContentDescription("lngItem ${desireLng.name}")
            .assertExists()
            .performClick()

        Assert.assertTrue(isLngChanged)
    }
}