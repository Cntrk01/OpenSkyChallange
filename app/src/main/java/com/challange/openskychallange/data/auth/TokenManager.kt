package com.challange.openskychallange.data.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor() {
    private var accessToken: String? = null
    private var expiryTime: Long = 0

    fun saveToken(token: String, expiresIn: Int) {
        accessToken = token
        expiryTime = System.currentTimeMillis() + (expiresIn - 60) * 1000L
    }

    fun getToken(): String? = if (System.currentTimeMillis() < expiryTime) accessToken else null
}