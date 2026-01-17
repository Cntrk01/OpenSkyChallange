package com.challange.openskychallange.data.auth

import javax.inject.Inject
import javax.inject.Singleton

/**
 * To access the OpenSky service, I need to obtain Bearer tokens.
 * Each token is valid for 30 minutes.
 * I've stored the tokens I received in this class so I can use them within the application.
 * */
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