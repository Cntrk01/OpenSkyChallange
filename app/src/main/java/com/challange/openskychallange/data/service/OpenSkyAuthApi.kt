package com.challange.openskychallange.data.service

import com.challange.openskychallange.domain.models.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OpenSkyAuthApi {
    //TODO :I'm adding hardcoded keys because it's not reading the keys
    //TODO :I entered into local.properties. I'll try to fix it if I have time.
    @FormUrlEncoded
    @POST("auth/realms/opensky-network/protocol/openid-connect/token")
    fun getAccessTokenSync(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String = "mustafa011001-api-client",
        @Field("client_secret") clientSecret: String = "6qR1N0kHNuZmnKR7wgnT3CF2k26qoqEs",
    ): retrofit2.Call<TokenResponse>
}