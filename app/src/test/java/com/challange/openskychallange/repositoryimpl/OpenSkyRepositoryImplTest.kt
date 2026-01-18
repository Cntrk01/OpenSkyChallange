package com.challange.openskychallange.repositoryimpl

import com.challange.openskychallange.data.repository.OpenSkyRepositoryImpl
import com.challange.openskychallange.data.service.OpenSkyApi
import com.challange.openskychallange.domain.models.OpenSkyServiceModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify

class OpenSkyRepositoryImplTest {
    private lateinit var api: OpenSkyApi
    private lateinit var repository: OpenSkyRepositoryImpl

    @Before
    fun setup() {
        api = mock()
        repository = OpenSkyRepositoryImpl(api)
    }

    @Test
    fun `getStates when called should return data from api`() = runTest {
        val lamin = 40.0; val lomin = 28.0; val lamax = 42.0; val lomax = 30.0
        val mockResponse = OpenSkyServiceModel(
            time = 123456789,
            states = listOf(listOf("icao24", "callsign", "origin_country"))
        )

        whenever(api.getFlightsInRegion(lamin, lomin, lamax, lomax))
            .thenReturn(mockResponse)

        val result = repository.getStates(lamin, lomin, lamax, lomax)

        assertEquals(mockResponse, result)

        verify(api).getFlightsInRegion(lamin, lomin, lamax, lomax)
    }

    @Test(expected = Exception::class)
    fun `getStates when api throws exception should propagate exception`() = runTest {
        whenever(api.getFlightsInRegion(0.0, 0.0, 0.0, 0.0))
            .thenThrow(RuntimeException("Network Error"))

        repository.getStates(0.0, 0.0, 0.0, 0.0)
    }
}