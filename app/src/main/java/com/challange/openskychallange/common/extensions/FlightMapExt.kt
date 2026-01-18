package com.challange.openskychallange.common.extensions

import com.google.android.gms.maps.model.LatLng

fun lerpLatLng(
    from: LatLng,
    to: LatLng,
    fraction: Float
): LatLng {
    return LatLng(
        lerp(from.latitude, to.latitude, fraction),
        lerp(from.longitude, to.longitude, fraction)
    )
}
fun lerp(start: Double, end: Double, fraction: Float): Double {
    return start + (end - start) * fraction
}

fun epsilonForZoom(zoom: Float): Double {
    return when {
        zoom < 6f  -> 0.05     // ~5 km
        zoom < 8f  -> 0.01     // ~1 km
        zoom < 10f -> 0.002    // ~200 m
        zoom < 12f -> 0.0005   // ~50 m
        zoom < 14f -> 0.0001   // ~10 m
        else       -> 0.00003  // ~3 m
    }
}

fun LatLng.isCloseTo(
    other: LatLng,
    epsilon: Double
): Boolean {
    return kotlin.math.abs(latitude - other.latitude) < epsilon &&
            kotlin.math.abs(longitude - other.longitude) < epsilon
}
