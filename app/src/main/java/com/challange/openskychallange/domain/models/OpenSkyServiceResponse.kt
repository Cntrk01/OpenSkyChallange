package com.challange.openskychallange.domain.models

/**
 * This class represents the raw API response returned by the OpenSky service.
 * The states field contains aircraft data as index-based lists and must be mapped to domain models before being used.
 * */
data class OpenSkyServiceResponse(
    val time: Int,
    val states: List<List<Any>>
)