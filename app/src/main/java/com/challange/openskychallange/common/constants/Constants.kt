package com.challange.openskychallange.common.constants

import com.challange.openskychallange.BuildConfig

object Constants {
    private const val USER_NAME = BuildConfig.CLIENT_ID
    private const val PASSWORD = BuildConfig.CLIENT_SECRET_KEY
    const val CREDENTIALS = "${USER_NAME}:${PASSWORD}"
}