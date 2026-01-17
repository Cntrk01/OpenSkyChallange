package com.challange.openskychallange.data.di

import com.challange.openskychallange.data.repository.OpenSkyAuthRepositoryRepositoryImpl
import com.challange.openskychallange.data.repository.OpenSkyRepositoryImpl
import com.challange.openskychallange.domain.repository.OpenSkyAuthRepository
import com.challange.openskychallange.domain.repository.OpenSkyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindOpenSkyRepository(
        openSkyRepositoryImpl: OpenSkyRepositoryImpl
    ): OpenSkyRepository

    @Binds
    abstract fun bindOpenSkyRepositoryAuth(
        openSkyAuthRepositoryImpl: OpenSkyAuthRepositoryRepositoryImpl
    ): OpenSkyAuthRepository
}