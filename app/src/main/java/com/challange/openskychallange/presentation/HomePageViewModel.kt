package com.challange.openskychallange.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challange.openskychallange.common.response.Response
import com.challange.openskychallange.domain.models.FlightUiModel
import com.challange.openskychallange.domain.models.GeoBounds
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

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val openSkyUseCase: OpenSkyUseCase
): ViewModel(){

    private var counterJob: Job? = null
    private var currentGeoBounds: GeoBounds? = null

    private val selectedCountry = MutableStateFlow<String?>(null)
    private var allFlights: List<FlightUiModel> = emptyList()

    private val _uiState = MutableStateFlow(HomePageUiState())
    val uiState: StateFlow<HomePageUiState> = _uiState.asStateFlow()

    private val _countries = MutableStateFlow<List<String>>(emptyList())
    val countries: StateFlow<List<String>> = _countries.asStateFlow()

    /**
     * Triggered when the visible map bounds change.
     *
     * Receives the new bounding box values after user zoom or pan actions.
     * If the bounds have not changed, no new request is made.
     * If bounds are updated:
     *  - Fetches new airplane data
     *  - Starts automatic polling every 10 seconds
     */
    fun onBoundsChanged(
        lamin: Double,
        lomin: Double,
        lamax: Double,
        lomax: Double
    ) {
        val newGeoBounds = GeoBounds(lamin, lomin, lamax, lomax)

        if (newGeoBounds == currentGeoBounds) return

        currentGeoBounds = newGeoBounds

        getAirplanes(newGeoBounds)

        startPolling()
    }

    /**
     * Updates the selected country state.
     *
     * If "All" is selected, country filtering is disabled.
     * After updating the selection, the flight list is re-filtered.
     */
    fun selectedCountry(country: String) {
        selectedCountry.value = if (country == "All") null else country
        applyFilters()
    }

    /**
     * Filters the flight list based on the selected country.
     *
     * If no country is selected, all flights are displayed.
     * The filtered result is reflected in the UI state.
     */
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

    /**
     * Starts automatic polling to fetch airplane data every 10 seconds.
     *
     * Cancels any previously running polling job.
     * Polling continues as long as the ViewModel is active.
     */
    private fun startPolling() {
        counterJob?.cancel()
        counterJob = viewModelScope.launch {
            while (isActive) {
                delay(10_000)
                currentGeoBounds?.let { getAirplanes(it) }
            }
        }
    }

    /**
     * Fetches airplane data from the OpenSky service
     * for the given map bounds.
     *
     * Observes the API response as a Flow and:
     *  - Updates UI to loading state
     *  - Displays error messages on failure
     *  - Updates flight list on success
     */
    private fun getAirplanes(geoBounds: GeoBounds) =
        viewModelScope.launch(Dispatchers.IO) {
            openSkyUseCase.getAirplane(
                geoBounds.lamin,
                geoBounds.lomin,
                geoBounds.lamax,
                geoBounds.lomax
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

    /**
     * Extracts unique country names from the flight list.
     *
     * The country list is created only once after the first successful data load.
     * An "All" option is added at the beginning of the list.
     */
    private fun updateCountryList(flights: List<FlightUiModel>) {
        if (_countries.value.isNotEmpty()) return
        val uniqueCountries = flights
            .map { it.originCountry ?: "Unknown Country" }
            .distinct()
            .sorted()

        _countries.value = listOf("All") + uniqueCountries
    }
}