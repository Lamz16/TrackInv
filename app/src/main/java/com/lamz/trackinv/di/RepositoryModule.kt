package com.lamz.trackinv.di

import com.lamz.trackinv.data.repositories.TrackRepositoryImpl
import com.lamz.trackinv.domain.repository.TrackRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideTaskRepository(trackRepositoryImpl: TrackRepositoryImpl): TrackRepository
}