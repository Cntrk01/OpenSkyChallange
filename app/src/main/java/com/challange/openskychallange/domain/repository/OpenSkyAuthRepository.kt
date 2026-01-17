package com.challange.openskychallange.domain.repository

import okhttp3.Authenticator

interface OpenSkyAuthRepository : Authenticator{
    fun fetchNewToken(): String?
}