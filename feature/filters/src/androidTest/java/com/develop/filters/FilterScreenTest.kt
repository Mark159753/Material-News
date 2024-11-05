package com.develop.filters

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.develop.data.models.filters.SortBy
import com.develop.data.models.spurces.generateDummyModel
import com.develop.filters.ui.FilterRoute
import com.develop.filters.ui.state.FilterScreenUIState
import com.develop.ui.theme.MaterialNewsTheme
import com.develop.ui.util.actions.CommonAction
import com.develop.ui.util.actions.UIActionsImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class FilterScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun check_if_save_btn_enabled() {
        composeTestRule.setContent {
            MaterialNewsTheme {
                FilterRoute(
                    filterState = FilterScreenUIState(
                        anyChanges = MutableStateFlow(true)
                    ),
                )
            }
        }

        composeTestRule.onNodeWithTag(
            composeTestRule.activity.resources.getString(R.string.filter_screen_save_btn)
        )
            .assertExists()
            .assertIsEnabled()
    }

    @Test
    fun select_source() {
        val sourcesList = List(5){generateDummyModel(it)}
        val item = sourcesList.last()

        var isItemSelected = false

        composeTestRule.setContent {
            MaterialNewsTheme {
                FilterRoute(
                    filterState = FilterScreenUIState(
                        anyChanges = MutableStateFlow(true),
                        sourcesList = MutableStateFlow(sourcesList)
                    ),
                    onSelectSource = {
                        isItemSelected = it.id == item.id
                    }
                )
            }
        }

        composeTestRule.onNodeWithTag(item.id)
            .assertExists()
            .performClick()

        Assert.assertTrue(isItemSelected)
    }

    @Test
    fun select_sort_order() {
        val order = SortBy.POPULARITY

        var isItemSelected = false

        composeTestRule.setContent {
            MaterialNewsTheme {
                FilterRoute(
                    filterState = FilterScreenUIState(
                        anyChanges = MutableStateFlow(true),
                    ),
                    onSetSortOrder = { isItemSelected = it == order }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription(order.name)
            .assertExists()
            .performClick()

        Assert.assertTrue(isItemSelected)
    }

    @Test
    fun check_on_nav_back_action() {
        val uiAction = UIActionsImpl()

        var isNavBackClicked = false

        composeTestRule.setContent {
            MaterialNewsTheme {
                FilterRoute(
                    onNavBack = { isNavBackClicked = true },
                    uiActions = uiAction
                )
            }
        }
        runBlocking { uiAction.sendAction(CommonAction.NavBack) }

        Assert.assertTrue(isNavBackClicked)
    }
}