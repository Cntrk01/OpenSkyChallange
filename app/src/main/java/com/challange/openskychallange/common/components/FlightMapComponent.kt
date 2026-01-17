package com.challange.openskychallange.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challange.openskychallange.R
import com.challange.openskychallange.domain.models.FlightUiModel
import com.challange.openskychallange.common.extensions.bitmapDescriptorFromResource
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun FlightMapComponent(
    flights: List<FlightUiModel>,
    zoomScale : Float = 13f,
    onCameraMoving: (Boolean) -> Unit = {},
    onBoundsChanged: (lamin: Double, lomin: Double, lamax: Double, lomax: Double) -> Unit,
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.015137, 28.979530), zoomScale)
    }

    var onMapLoaded by remember { mutableStateOf(false) }
    var selectedFlightIcao by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    LaunchedEffect(zoomScale) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.zoomTo(zoomScale),
            durationMs = 1000
        )
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        onCameraMoving(cameraPositionState.isMoving)

        if (!cameraPositionState.isMoving) {
            val projection = cameraPositionState.projection
            val visibleRegion = projection?.visibleRegion

            visibleRegion?.let { region ->
                val bounds = region.latLngBounds
                onBoundsChanged(
                    bounds.southwest.latitude,
                    bounds.southwest.longitude,
                    bounds.northeast.latitude,
                    bounds.northeast.longitude
                )
            }
        }
    }
    var planeIcon by remember { mutableStateOf<BitmapDescriptor?>(null) }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { selectedFlightIcao = null },
        onMapLoaded = {
            onMapLoaded = true
            planeIcon = context.bitmapDescriptorFromResource(R.drawable.icon_airplane, 90, 90)
        },
    ) {

            flights.forEach { flight ->
                val markerState = remember(flight.icao24) {
                    MarkerState(LatLng(flight.lat, flight.lon))
                }

                LaunchedEffect(selectedFlightIcao) {
                    if (selectedFlightIcao == flight.icao24) {
                        markerState.showInfoWindow()
                    } else {
                        markerState.hideInfoWindow()
                    }
                }

                MarkerInfoWindowContent(
                    icon = planeIcon,
                    state = markerState,
                    rotation = flight.heading ?: 0f,
                    anchor = Offset(0.5f, 0.5f),
                    onClick = {
                        selectedFlightIcao = if (selectedFlightIcao == flight.icao24) null else flight.icao24
                        true
                    },
                    content = {
                        FlightInfoWindow(flight)
                    }
                )

        }
    }
}

@Composable
private fun FlightInfoWindow(flight: FlightUiModel) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .widthIn(min = 160.dp)
    ) {
        Column {
            Text(
                text = flight.callsign ?: "Unknown Fly",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Country: ${flight.originCountry}")
            Text(text = "ICAO24: ${flight.icao24}")
            Text(text = "Velocity: ${flight.velocity}")
            flight.heading?.let {
                Text(text = "Heading: ${it.toInt()}°")
            }
        }
    }
}

@Preview
@Composable
private fun FlightMapComponentPreview(){
    val flights = listOf(
        FlightUiModel(
            icao24 = "abc123",
            callsign = "THY123",
            originCountry = "Turkey",
            lat = 41.015137,
            lon = 28.979530,
            heading = 360f,
            velocity = 220.0,
        ),
        FlightUiModel(
            icao24 = "xyz789",
            callsign = "PGT456",
            originCountry = "Turkey",
            lat = 41.065137,
            lon = 28.579530,
            heading = 90f,
            velocity = 320.0,
        )
    )
    FlightMapComponent(
        flights = flights,
        onBoundsChanged = { lamin, lomin, lamax, lomax ->

        }
    )
}