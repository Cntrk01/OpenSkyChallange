package com.challange.openskychallange

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.challange.openskychallange.common.components.MapZoomDropdown
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapZoomDropdownTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    private val zoomOptions = listOf(3f, 5f, 7f, 9f, 11f, 13f, 15f, 17f, 19f)

    @Test
    fun initial_state_shows_correct_selected_zoom_value() {
        val initialZoom = 7f

        composeTestRule.setContent {
            MapZoomDropdown(
                selectedZoom = initialZoom,
                onZoomSelected = {}
            )
        }

        composeTestRule.onNodeWithText("7.0").assertIsDisplayed()

        composeTestRule.onNodeWithText("15.0").assertDoesNotExist()
    }

    @Test
    fun clicking_dropdown_opens_menu_with_all_zoom_options() {
        composeTestRule.setContent {
            MapZoomDropdown(
                selectedZoom = 3f,
                onZoomSelected = {}
            )
        }

        composeTestRule.onNodeWithText("Map Zoom").performClick()

        zoomOptions.forEach { zoom ->
            composeTestRule.onAllNodesWithText(zoom.toString()).onFirst().assertIsDisplayed()
        }
    }

    @Test
    fun selecting_a_zoom_option_triggers_callback_and_closes_menu() {
        var capturedZoom = 0f
        val targetZoom = 13f

        composeTestRule.setContent {
            MapZoomDropdown(
                selectedZoom = 3f,
                onZoomSelected = { capturedZoom = it }
            )
        }

        composeTestRule.onNodeWithText("Map Zoom").performClick()
        composeTestRule.onAllNodesWithText(targetZoom.toString()).onFirst().performClick()

        assert(capturedZoom == targetZoom)

        composeTestRule.onNodeWithText("19.0").assertDoesNotExist()
    }

    @Test
    fun clicking_outside_closes_the_zoom_menu() {
        composeTestRule.setContent {
            MapZoomDropdown(
                selectedZoom = 3f,
                onZoomSelected = {}
            )
        }

        composeTestRule.onNodeWithText("Map Zoom").performClick()
        composeTestRule.onAllNodesWithText("11.0").onFirst().assertIsDisplayed()

        composeTestRule.onAllNodes(isRoot()).onFirst().performTouchInput {
            click(position = Offset(1f, 1f))
        }

        composeTestRule.onNodeWithText("11.0").assertDoesNotExist()
    }
}