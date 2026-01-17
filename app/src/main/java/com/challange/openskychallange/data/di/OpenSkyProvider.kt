package com.challange.openskychallange.data.di

import com.challange.openskychallange.data.auth.TokenManager
import com.challange.openskychallange.data.service.OpenSkyApi
import com.challange.openskychallange.data.service.OpenSkyAuthApi
import com.challange.openskychallange.domain.repository.OpenSkyAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OpenSkyProvider {

    @Provides
    @Singleton
    fun provideAuthApi(): OpenSkyAuthApi {
        return Retrofit.Builder()
            .baseUrl("https://auth.opensky-network.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenSkyAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMainOkHttpClient(
        authRepository: OpenSkyAuthRepository,
        tokenManager: TokenManager
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .authenticator(authRepository)
            .addInterceptor { chain ->
                println("API_LOG: --- İstek Hazırlanıyor ---")

                var token = tokenManager.getToken()

                if (token == null) {
                    token = authRepository.fetchNewToken()
                }

                val requestBuilder = chain.request().newBuilder()

                if (token != null) {
                    requestBuilder.header("Authorization", "Bearer $token")
                } else {
                    println("API_LOG: HATA - Token alınamadı, istek auth olmadan atılıyor!")
                }

                requestBuilder.header("Content-Type", "application/json")
                requestBuilder.header("Accept", "application/json")

                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://opensky-network.org/api/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOpenSkyApi(retrofit: Retrofit) : OpenSkyApi {
        return retrofit.create(OpenSkyApi::class.java)
    }
}