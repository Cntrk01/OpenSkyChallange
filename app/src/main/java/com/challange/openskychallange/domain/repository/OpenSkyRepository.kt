package com.challange.openskychallange.domain.repository

import com.challange.openskychallange.domain.models.OpenSkyServiceModel

interface OpenSkyRepository {
    suspend fun getStates(lamin: Double,
                          lomin: Double,
                          lamax: Double,
                          lomax: Double) : OpenSkyServiceModel
}