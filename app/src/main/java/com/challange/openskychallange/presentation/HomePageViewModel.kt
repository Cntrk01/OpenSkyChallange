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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

interface data class Bounds(
    val lamin: Double,
    val lomin: Double,
    val lamax: Double,
    val lomax: Double
)

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val openSkyUseCase: OpenSkyUseCase
): ViewModel(){

    private var pollingJob: Job? = null
    private var currentBounds: Bounds? = null

    private val selectedCountry = MutableStateFlow<String?>(null)
    private var allFlights: List<FlightUiModel> = emptyList()

    private val _uiState = MutableStateFlow(HomePageUiState())
    val uiState: StateFlow<HomePageUiState> = _uiState.asStateFlow()

    private val _countries = MutableStateFlow<List<String>>(emptyList())
    val countries: StateFlow<List<String>> = _countries.asStateFlow()

    fun onBoundsChanged(
        lamin: Double,
        lomin: Double,
        lamax: Double,
        lomax: Double
    ) {
        val newBounds = Bounds(lamin, lomin, lamax, lomax)

        if (newBounds == currentBounds) return

        currentBounds = newBounds

        getAirplanes(newBounds)

        startPolling()
    }

    fun selectedCountry(country: String) {
        selectedCountry.value = if (country == "All") null else country
        applyFilters()
    }

    private fun applyFilters() {
        val country = selectedCountry.value

        val filteredList = if (country.isNullOrEmpty()) {
            allFlights
        } else {
            allFlights.filter { it.originCountry == country }
        }

        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = null,
                airplaneList = filteredList
            )
        }
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (isActive) {
                delay(10_000)
                currentBounds?.let { getAirplanes(it) }
            }
        }
    }

    private fun getAirplanes(bounds: Bounds) =
        viewModelScope.launch(Dispatchers.IO) {
            openSkyUseCase.getAirplane(
                bounds.lamin,
                bounds.lomin,
                bounds.lamax,
                bounds.lomax
            ).collectLatest { response ->
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
                        allFlights = response.data
                        updateCountryList(allFlights)
                        applyFilters()
                    }
                }
            }
        }

    private fun updateCountryList(flights: List<FlightUiModel>) {
        if (_countries.value.isNotEmpty()) return
        val uniqueCountries = flights
            .map { it.originCountry ?: "Unknown Country" }
            .distinct()
            .sorted()

        _countries.value = listOf("All") + uniqueCountries
    }
}