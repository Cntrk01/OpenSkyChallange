package com.challange.openskychallange.data.repository

import com.challange.openskychallange.data.auth.TokenManager
import com.challange.openskychallange.data.service.OpenSkyAuthApi
import com.challange.openskychallange.domain.repository.OpenSkyAuthRepository
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class OpenSkyAuthRepositoryRepositoryImpl @Inject constructor(
    private val authApi: OpenSkyAuthApi,
    private val tokenManager: TokenManager
) : OpenSkyAuthRepository {

    override fun fetchNewToken(): String? {
        return try {
            val response = authApi.getAccessTokenSync().execute()

            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    tokenManager.saveToken(it.accessToken, it.expiresIn)
                    it.accessToken
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val newToken = fetchNewToken()
        return if (newToken != null) {
            response.request.newBuilder()
                .header("Authorization", "Bearer $newToken")
                .build()
        } else null
    }
}