package com.challange.openskychallange.domain.repository

import com.challange.openskychallange.domain.models.OpenSkyServiceResponse

interface OpenSkyRepository {
    suspend fun getStates(
        lamin: Double,
        lomin: Double,
        lamax: Double,
        lomax: Double,
    ) : OpenSkyServiceResponse
}