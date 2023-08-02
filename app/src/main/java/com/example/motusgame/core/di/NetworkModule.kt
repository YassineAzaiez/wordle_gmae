package com.example.motusgame.core.di

import android.content.Context
import com.example.motusgame.core.utils.networkUtils.MotusGameApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Singleton
    @Provides
    fun provideHttpClient(cache: Cache): OkHttpClient =
        MotusGameApiClient.providesOkHttpClient(cache)

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context) =
        MotusGameApiClient.providesOkHttpCache(context)

    @Singleton
    @Provides
    fun providRetrofit(httpClient: OkHttpClient) =
        MotusGameApiClient.providesRetrofitInstance(httpClient)


}