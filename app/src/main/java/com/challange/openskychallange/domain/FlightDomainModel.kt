package com.challange.openskychallange.domain

data class FlightDomainModel(
    val icao24: String,
    val callsign: String?,
    val originCountry: String?,
    val timePosition: Int?,
    val lastContact: Int?,
    val longitude: Double?,
    val latitude: Double?,
    val baroAltitude: Double?,
    val onGround: Boolean,
    val velocity: Double?,
    val trueTrack: Double?,
    val verticalRate: Double?,
    val sensors: List<Int>?,
    val geoAltitude: Double?,
    val squawk: String?,
    val spi: Boolean?,
    val positionSource: Int?,
    val category: Int?
)

fun mapStateToFlight(state: List<Any>): FlightDomainModel {
    return FlightDomainModel(
        icao24 = state.getOrNull(0)?.toString() ?: "",
        callsign = state.getOrNull(1)?.toString(),
        originCountry = state.getOrNull(2)?.toString(),
        timePosition = (state.getOrNull(3) as? Number)?.toInt(),
        lastContact = (state.getOrNull(4) as? Number)?.toInt(),
        longitude = (state.getOrNull(5) as? Number)?.toDouble(),
        latitude = (state.getOrNull(6) as? Number)?.toDouble(),
        baroAltitude = (state.getOrNull(7) as? Number)?.toDouble(),
        onGround = state.getOrNull(8) as? Boolean ?: false,
        velocity = (state.getOrNull(9) as? Number)?.toDouble(),
        trueTrack = (state.getOrNull(10) as? Number)?.toDouble(),
        verticalRate = (state.getOrNull(11) as? Number)?.toDouble(),
        sensors = (state.getOrNull(12) as? List<*>)?.mapNotNull { (it as? Number)?.toInt() },
        geoAltitude = (state.getOrNull(13) as? Number)?.toDouble(),
        squawk = state.getOrNull(14)?.toString(),
        spi = state.getOrNull(15) as? Boolean,
        positionSource = (state.getOrNull(16) as? Number)?.toInt(),
        category = (state.getOrNull(17) as? Number)?.toInt()
    )
}

