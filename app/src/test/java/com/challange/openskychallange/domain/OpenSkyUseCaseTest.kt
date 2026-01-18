package com.challange.openskychallange.domain

import com.challange.openskychallange.common.response.Response
import com.challange.openskychallange.domain.models.OpenSkyServiceModel
import com.challange.openskychallange.domain.repository.OpenSkyRepository
import com.challange.openskychallange.domain.usecase.OpenSkyUseCase
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class OpenSkyUseCaseTest {

    private lateinit var repository: OpenSkyRepository
    private lateinit var useCase: OpenSkyUseCase

    @Before
    fun setup() {
        repository = mock()
        useCase = OpenSkyUseCase(repository)
    }

    @Test
    fun `getAirplane when repository returns states should emit Loading then Success`() = runTest {

        val lamin = 40.0
        val lomin = 28.0
        val lamax = 42.0
        val lomax = 30.0

        val mockServiceResponse = OpenSkyServiceModel(
            time = 12345,
            states = listOf(
                listOf("4bb111", "THY123", "Turkey", 1705584000, 1705584000, 28.0, 41.0, 10000.0, false, 250.0, 90.0, 0.0)
            )
        )

        whenever(repository.getStates(lamin, lomin, lamax, lomax)).thenReturn(mockServiceResponse)

        val results = useCase.getAirplane(lamin, lomin, lamax, lomax).toList()

        assertEquals(Response.Loading, results[0])

        assertTrue(results[1] is Response.Success)

        val successData = (results[1] as Response.Success).data
        assertEquals(1, successData.size)
        assertEquals("THY123", successData[0].callsign?.trim())
    }

    @Test
    fun `getAirplane when repository throws exception should emit Loading then Failure`() = runTest {
        val exception = RuntimeException("Network Error")
        whenever(repository.getStates(0.0, 0.0, 0.0, 0.0)).thenThrow(exception)

        val results = useCase.getAirplane(0.0, 0.0, 0.0, 0.0).toList()

        assertEquals(Response.Loading, results[0])
        assertTrue(results[1] is Response.Failure)

        val failure = results[1] as Response.Failure
        assertEquals("No aircraft were found in the vicinity. Please change your location.", failure.errorMessage)
    }
}