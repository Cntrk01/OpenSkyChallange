package com.challange.openskychallange

import com.challange.openskychallange.data.auth.TokenManager
import com.challange.openskychallange.data.repository.OpenSkyAuthRepositoryRepositoryImpl
import com.challange.openskychallange.data.service.OpenSkyAuthApi
import com.challange.openskychallange.domain.models.TokenResponse
import junit.framework.TestCase.assertEquals
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Response as RetrofitResponse

class OpenSkyAuthRepositoryImplTest {

    private lateinit var authApi: OpenSkyAuthApi
    private lateinit var tokenManager: TokenManager
    private lateinit var repository: OpenSkyAuthRepositoryRepositoryImpl

    @Before
    fun setup() {
        authApi = mock()
        tokenManager = mock()
        repository = OpenSkyAuthRepositoryRepositoryImpl(authApi, tokenManager)
    }

    @Test
    fun `fetchNewToken - when api call is successful - returns token and saves it`() {
        val mockTokenResponse = TokenResponse(accessToken = "new_token_123", expiresIn = 3600)
        val mockCall = mock<Call<TokenResponse>>()

        whenever(authApi.getAccessTokenSync()).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(RetrofitResponse.success(mockTokenResponse))

        val result = repository.fetchNewToken()

        assertEquals("new_token_123", result)
        verify(tokenManager).saveToken("new_token_123", 3600)
    }

    @Test
    fun `fetchNewToken - when api call fails - returns null`() {
        val mockCall = mock<Call<TokenResponse>>()

        val errorResponseBody = "".toResponseBody(null)
        val errorResponse = RetrofitResponse.error<TokenResponse>(401, errorResponseBody)

        doReturn(mockCall).whenever(authApi).getAccessTokenSync()
        doReturn(errorResponse).whenever(mockCall).execute()

        val result = repository.fetchNewToken()

        assertNull(result)
        verifyNoInteractions(tokenManager)
    }

    @Test
    fun `authenticate - when fetchNewToken succeeds - returns request with bearer header`() {
        val mockTokenResponse = TokenResponse("valid_token", 3600)
        val mockCall = mock<Call<TokenResponse>>()
        whenever(authApi.getAccessTokenSync()).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(RetrofitResponse.success(mockTokenResponse))

        val originalRequest = Request.Builder().url("https://api.test.com").build()
        val mockResponse = mock<Response> {
            on { request } doReturn originalRequest
        }
        val authenticatedRequest = repository.authenticate(null, mockResponse)

        assertNotNull(authenticatedRequest)
        assertEquals("Bearer valid_token", authenticatedRequest?.header("Authorization"))
    }

    @Test
    fun `authenticate - when fetchNewToken fails - returns null`() {
        val mockCall = mock<Call<TokenResponse>>()
        whenever(authApi.getAccessTokenSync()).thenReturn(mockCall)
        whenever(mockCall.execute()).thenThrow(RuntimeException("Network Error"))

        val mockResponse = mock<Response> {
            on { request } doReturn Request.Builder().url("https://api.test.com").build()
        }

        val result = repository.authenticate(null, mockResponse)

        assertNull(result)
    }
}