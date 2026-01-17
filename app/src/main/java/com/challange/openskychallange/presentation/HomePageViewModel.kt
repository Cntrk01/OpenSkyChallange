package com.challange.openskychallange.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challange.openskychallange.common.response.Response
import com.challange.openskychallange.domain.models.FlightUiModel
import com.challange.openskychallange.domain.usecase.OpenSkyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.sorted

data class HomePageUiState(
    val isLoading : Boolean = false,
    val errorMessage : String? = null,
    val airplaneList : List<FlightUiModel> = emptyList(),
)

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val openSkyUseCase: OpenSkyUseCase
): ViewModel(){

    private var refreshJob: Job? = null

    private var _isMoving = MutableStateFlow(false)

    private val _uiState = MutableStateFlow(HomePageUiState())
    val uiState: StateFlow<HomePageUiState> = _uiState.asStateFlow()

    private val _countries = MutableStateFlow<List<String>>(emptyList())
    val countries: StateFlow<List<String>> = _countries.asStateFlow()

    fun getAirplanes(lamin: Double, lomin: Double, lamax: Double, lomax: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            openSkyUseCase.getAirplane(lamin, lomin, lamax, lomax).collectLatest { response ->
                when (response) {
                    is Response.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Response.Failure -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = response.errorMessage)
                        }
                    }
                    is Response.Success -> {
                        val allFlights = response.data

                        updateCountryList(allFlights)

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                airplaneList = allFlights,
                            )
                        }
                    }
                }
            }
        }

    fun onCameraMoved(lamin: Double, lomin: Double, lamax: Double, lomax: Double) {
        refreshJob?.cancel()

        refreshJob = viewModelScope.launch(Dispatchers.IO) {
            while (!_isMoving.value){
                delay(10000L)

                getAirplanes(lamin, lomin, lamax, lomax)
            }
        }
    }

    fun selectedCountry(country: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                airplaneList = it.airplaneList.filter { flight ->
                    flight.originCountry == country
                }
            )
        }
    }

    fun onCameraMoving(isMoving: Boolean) {
        _isMoving.value = isMoving
    }

    private fun updateCountryList(flights: List<FlightUiModel>) {
        val uniqueCountries = flights
            .map { it.originCountry ?: "Unknown Country" }
            .distinct()
            .sorted()

        _countries.value = uniqueCountries
    }
}