package com.challange.openskychallange.domain.repository

import com.challange.openskychallange.domain.models.ServiceModel

interface OpenSkyRepository {
    suspend fun getStates() : ServiceModel
}