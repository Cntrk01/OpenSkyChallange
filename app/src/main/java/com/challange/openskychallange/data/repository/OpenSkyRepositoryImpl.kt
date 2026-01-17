package com.challange.openskychallange.data.repository

import com.challange.openskychallange.data.service.OpenSkyApi
import com.challange.openskychallange.domain.models.OpenSkyServiceModel
import com.challange.openskychallange.domain.repository.OpenSkyRepository
import javax.inject.Inject

class OpenSkyRepositoryImpl @Inject constructor(
    private val api: OpenSkyApi
) : OpenSkyRepository {
    override suspend fun getStates(lamin: Double, lomin: Double, lamax: Double, lomax: Double): OpenSkyServiceModel {
        return api.getFlightsInRegion(lamin, lomin, lamax, lomax)
    }
}