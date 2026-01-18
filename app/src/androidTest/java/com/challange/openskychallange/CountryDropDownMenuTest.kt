package com.challange.openskychallange

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.challange.openskychallange.common.components.CountryDropDown
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryDropDownMenuTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    private val countryList = listOf("Turkey", "Germany", "USA", "France")

    @Test
    fun initial_state_should_display_the_default_country_and_keep_menu_closed() {
        composeTestRule.setContent {
            CountryDropDown(
                countries = countryList,
                defaultCountry = "Turkey"
            )
        }

        composeTestRule.onNodeWithText("Turkey").assertIsDisplayed()
        composeTestRule.onNodeWithText("Germany").assertDoesNotExist()
    }

    @Test
    fun clicking_the_text_field_should_expand_the_dropdown_menu_and_show_all_countries() {
        composeTestRule.setContent {
            CountryDropDown(countries = countryList, defaultCountry = "Select Country")
        }

        composeTestRule.onNodeWithText("Origin Country").performClick()

        countryList.forEach { country ->
            composeTestRule.onAllNodesWithText(country).onFirst().assertIsDisplayed()
        }
    }
    @Test
    fun selecting_an_item_should_update_the_displayed_text_and_trigger_callback() {
        var selectedResult = ""

        composeTestRule.setContent {
            CountryDropDown(
                countries = countryList,
                onCountrySelected = { selectedResult = it }
            )
        }

        composeTestRule.onNodeWithText("Origin Country").performClick()
        composeTestRule.onNodeWithText("Germany").performClick()

        assert(selectedResult == "Germany")
        composeTestRule.onNodeWithText("Germany").assertIsDisplayed()
    }

    @Test
    fun menu_should_close_automatically_after_a_country_is_selected() {
        composeTestRule.setContent {
            CountryDropDown(countries = countryList)
        }

        composeTestRule.onNodeWithText("Origin Country").performClick()
        composeTestRule.onNodeWithText("USA").performClick()

        composeTestRule.onNodeWithText("Turkey").assertDoesNotExist()
    }

    @Test
    fun clicking_outside_the_menu_should_trigger_dismissal_and_close_the_menu() {
        composeTestRule.setContent {
            CountryDropDown(countries = countryList, defaultCountry = "Turkey")
        }

        composeTestRule.onNodeWithText("Origin Country").performClick()
        composeTestRule.onAllNodesWithText("France").onFirst().assertIsDisplayed()

        composeTestRule.onAllNodes(isRoot()).onFirst().performTouchInput {
            click(position = androidx.compose.ui.geometry.Offset(1f, 1f))
        }

        composeTestRule.onAllNodesWithText("France").assertCountEquals(0)
    }
}