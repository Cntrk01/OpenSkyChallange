package com.challange.openskychallange.domain.models

data class FlightUiModel(
    val icao24: String,
    val callsign: String?,
    val originCountry: String?,
    val lat: Double,
    val lon: Double,
    val heading: Float?,
    val velocity: Double?,
)

fun FlightDomainModel.toUiModel() = FlightUiModel(
    icao24 = icao24,
    callsign = callsign,
    originCountry = originCountry,
    lat = latitude ?: 0.0,
    lon = longitude ?: 0.0,
    heading = trueTrack?.toFloat(),
    velocity = velocity,
)