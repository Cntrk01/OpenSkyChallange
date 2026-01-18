package com.challange.openskychallange.domain.models

import com.google.gson.annotations.SerializedName

/**
 * This is the class that returns the token and its expiration date when we send a token request.
 * */
data class TokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Int
)