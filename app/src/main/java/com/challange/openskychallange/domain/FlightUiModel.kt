package com.challange.openskychallange.domain

data class FlightUiModel(
    val icao24: String,
    val callsign: String?,
    val originCountry: String,
    val lat: Double,
    val lon: Double,
    val heading: Float?
)