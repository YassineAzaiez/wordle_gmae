package com.example.motusgame.core.di

import com.example.motusgame.data.api.RandomWordApi
import com.example.motusgame.data.dataSource.WordDataSource
import com.example.motusgame.data.dataSource.WordDataSourceImp
import com.example.motusgame.data.repository.WordRepositoryImp
import com.example.motusgame.domain.repository.WordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DataSourcesModule {

    @Provides
    fun provideAppRemoteDataSource(
        apiService: RandomWordApi
    ): WordDataSource = WordDataSourceImp(apiService)


    @Provides
    fun provideAppRepository(
        dataSource: WordDataSource,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): WordRepository = WordRepositoryImp(dataSource, ioDispatcher)

}