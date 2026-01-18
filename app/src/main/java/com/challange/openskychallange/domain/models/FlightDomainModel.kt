package com.challange.openskychallange.domain.models

/**
 * This class is essentially the one that will allow us to do the modeling that will come from the service.
 * */
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

/**
 * This method allows us to retrieve items from the list one by one and transform them into our model, since it comes with List<List<Any>>.
 * */
fun List<Any?>.toFlightDomainModel(): FlightDomainModel {
    return FlightDomainModel(
        icao24 = this.getOrNull(0)?.toString() ?: "",
        callsign = this.getOrNull(1)?.toString(),
        originCountry = this.getOrNull(2)?.toString(),
        timePosition = (this.getOrNull(3) as? Number)?.toInt(),
        lastContact = (this.getOrNull(4) as? Number)?.toInt(),
        longitude = (this.getOrNull(5) as? Number)?.toDouble(),
        latitude = (this.getOrNull(6) as? Number)?.toDouble(),
        baroAltitude = (this.getOrNull(7) as? Number)?.toDouble(),
        onGround = this.getOrNull(8) as? Boolean ?: false,
        velocity = (this.getOrNull(9) as? Number)?.toDouble(),
        trueTrack = (this.getOrNull(10) as? Number)?.toDouble(),
        verticalRate = (this.getOrNull(11) as? Number)?.toDouble(),
        sensors = (this.getOrNull(12) as? List<*>)?.mapNotNull { (it as? Number)?.toInt() },
        geoAltitude = (this.getOrNull(13) as? Number)?.toDouble(),
        squawk = this.getOrNull(14)?.toString(),
        spi = this.getOrNull(15) as? Boolean,
        positionSource = (this.getOrNull(16) as? Number)?.toInt(),
        category = (this.getOrNull(17) as? Number)?.toInt()
    )
}

