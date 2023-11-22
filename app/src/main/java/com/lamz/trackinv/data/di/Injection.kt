package com.lamz.trackinv.data.di

import android.content.Context
import com.lamz.trackinv.api.ApiConfig
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.pref.UserPreference
import com.lamz.trackinv.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): TrackRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return TrackRepository.getInstance(pref,apiService)
    }
}