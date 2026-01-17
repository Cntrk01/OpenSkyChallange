package com.challange.openskychallange

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.challange.openskychallange.components.FlightMapComponent
import com.challange.openskychallange.domain.FlightUiModel
import com.challange.openskychallange.ui.theme.OpenSkyChallangeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OpenSkyChallangeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlightMapComponent(
                        flights = listOf(
                            FlightUiModel(
                                icao24 = "abc123",
                                callsign = "THY123",
                                originCountry = "Turkey",
                                lat = 41.015137,
                                lon = 28.979530,
                                heading = 360f,
                                velocity = 220.2,
                            ),
                            FlightUiModel(
                                icao24 = "abc1234",
                                callsign = "THY123",
                                originCountry = "Turkey",
                                lat = 41.065137,
                                lon = 28.579530,
                                heading = 275f,
                                velocity = 240.2,
                            )
                        ),
                        onCameraMoving = {
                            println("Is Moving : $it")
                        },
                        onCameraIdle = {
                            println("Camera Idle : $it")
                        }
                    )
                }
            }
        }
    }
}