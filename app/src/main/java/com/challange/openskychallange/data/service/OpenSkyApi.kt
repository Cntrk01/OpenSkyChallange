package com.challange.openskychallange.data.service

import com.challange.openskychallange.domain.models.OpenSkyServiceModel
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenSkyApi {
    @GET("states/all")
    suspend fun getFlightsInRegion(
        @Query("lamin") lamin: Double,
        @Query("lomin") lomin: Double,
        @Query("lamax") lamax: Double,
        @Query("lomax") lomax: Double
    ): OpenSkyServiceModel
}