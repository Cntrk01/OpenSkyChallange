package com.challange.openskychallange.domain.models

/**
 * This method represents the model we want to display on the user screen.
 * */
data class FlightUiModel(
    val icao24: String,
    val callsign: String?,
    val originCountry: String?,
    val lat: Double,
    val lon: Double,
    val heading: Float?,
    val velocity: Double?,
)

/**
 * This helper extension method converts a domain model into a UI model by mapping only the fields required by the presentation layer.
 * */
fun FlightDomainModel.toUiModel() = FlightUiModel(
    icao24 = icao24,
    callsign = callsign,
    originCountry = originCountry,
    lat = latitude ?: 0.0,
    lon = longitude ?: 0.0,
    heading = trueTrack?.toFloat(),
    velocity = velocity,
)