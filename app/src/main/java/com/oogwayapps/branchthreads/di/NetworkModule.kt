package com.oogwayapps.branchthreads.di

import android.content.Context
import com.oogwayapps.branchthreads.BuildConfig
import com.oogwayapps.branchthreads.api.AuthInterceptor
import com.oogwayapps.branchthreads.api.ThreadsAPI
import com.oogwayapps.branchthreads.utils.Constants
import com.oogwayapps.branchthreads.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideApi(retrofit: Retrofit): ThreadsAPI {
        return retrofit
            .create(ThreadsAPI::class.java)
    }

    @Provides
    @Singleton
    internal fun retrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    internal fun client(connectivityInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(connectivityInterceptor)
            .build()

    @Provides
    @Singleton
    internal fun interceptor(tokenManager: TokenManager): AuthInterceptor = AuthInterceptor(tokenManager)

    @Provides
    @Singleton
    internal fun tokenManager(@ApplicationContext context: Context): TokenManager = TokenManager(context)
}


