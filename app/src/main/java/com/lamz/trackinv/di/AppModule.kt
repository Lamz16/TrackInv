package com.lamz.trackinv.di

import com.lamz.trackinv.domain.usecase.TrackInvUseCase
import com.lamz.trackinv.domain.usecase.TrackUseCaseInteract
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule{
    @Binds
    @Singleton
    abstract fun provideTrackUseCase(trackUseCaseInteract: TrackUseCaseInteract) : TrackInvUseCase
}
