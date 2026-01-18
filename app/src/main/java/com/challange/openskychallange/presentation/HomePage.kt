package com.challange.openskychallange.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challange.openskychallange.common.components.CountryDropDown
import com.challange.openskychallange.common.components.FlightMap
import com.challange.openskychallange.common.components.MapZoomDropdown

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    homeViewModel : HomePageViewModel = hiltViewModel()
) {
    val countryList by homeViewModel.countries.collectAsStateWithLifecycle()
    val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
    var selectedZoomScale by remember { mutableFloatStateOf(13f) }

    LaunchedEffect(Unit) {
        homeViewModel
            .onBoundsChanged(
                lamin = 40.0,
                lamax = 50.0,
                lomin = 20.0,
                lomax = 40.0,
            )
    }

    Column(
        modifier = modifier
         .fillMaxSize()
         .padding(8.dp)
    ) {
        Row {
            CountryDropDown(
              modifier = Modifier
                  .weight(2f),
              countries = countryList,
              onCountrySelected = {
                 homeViewModel.selectedCountry(country = it)
              }
            )

            Spacer(modifier = Modifier.width(6.dp))

            MapZoomDropdown(
                modifier = Modifier
                    .weight(1f),
                selectedZoom = selectedZoomScale,
            ) {
                selectedZoomScale = it
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            FlightMap(
                flights = uiState.airplaneList,
                zoomScale = selectedZoomScale,
                onCameraMoving = {

                },
                onBoundsChanged = { lamin, lomin, lamax, lomax ->
                    homeViewModel.onBoundsChanged(
                        lamin = lamin,
                        lamax = lamax,
                        lomin = lomin,
                        lomax = lomax,
                    )
                }
            )

            if (uiState.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            if (uiState.errorMessage?.isNotEmpty() == true){
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 10.dp),
                    text = uiState.errorMessage!!,
                    fontSize = 22.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color.Red,
                )
            }
        }
    }
}