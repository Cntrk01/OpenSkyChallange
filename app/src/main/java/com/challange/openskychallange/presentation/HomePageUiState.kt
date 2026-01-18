package com.challange.openskychallange.presentation

import com.challange.openskychallange.domain.models.FlightUiModel

/**
 * This class keeps track of the Home screen's states.
 * */
data class HomePageUiState(
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val airplaneList : List<FlightUiModel> = emptyList(),
)