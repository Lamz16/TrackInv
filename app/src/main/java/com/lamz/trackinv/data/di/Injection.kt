package com.lamz.trackinv.data.di

import android.content.Context
import com.lamz.trackinv.data.TrackRepository
import com.lamz.trackinv.data.pref.UserPreference
import com.lamz.trackinv.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): TrackRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return TrackRepository.getInstance(pref)
    }
}