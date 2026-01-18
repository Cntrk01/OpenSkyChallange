package com.challange.openskychallange.domain.usecase

import com.challange.openskychallange.common.response.Response
import com.challange.openskychallange.domain.models.FlightUiModel
import com.challange.openskychallange.domain.models.toFlightDomainModel
import com.challange.openskychallange.domain.models.toUiModel
import com.challange.openskychallange.domain.repository.OpenSkyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenSkyUseCase @Inject constructor(
    private val repository: OpenSkyRepository,
) {
    fun getAirplane(
        lamin: Double,
        lomin: Double,
        lamax: Double,
        lomax: Double
    ): Flow<Response<List<FlightUiModel>>> = flow {

        emit(Response.Loading)

        try {
            val response = repository.getStates(lamin, lomin, lamax, lomax)

            val states = response.states
            if (states.isNotEmpty()) {
                val uiModels = states.map { stateList ->
                    stateList
                        .toFlightDomainModel()
                        .toUiModel()
                }
                emit(Response.Success(uiModels))
            } else {
                emit(Response.Success(emptyList()))
            }
        } catch (e: Exception) {
            emit(
                Response.Failure(
                    exception = e,
                    errorMessage = "No aircraft were found in the vicinity. Please change your location."
                )
            )
        }
    }
}