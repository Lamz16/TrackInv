package com.lamz.trackinv.data

import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow

class TrackRepository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: TrackRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): TrackRepository =
            instance ?: synchronized(this) {
                instance ?: TrackRepository(userPreference)
            }.also { instance = it }
    }
}