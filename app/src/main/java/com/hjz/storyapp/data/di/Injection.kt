package com.hjz.storyapp.data.di

import android.content.Context
import com.hjz.storyapp.data.api.ApiConfigStory
import com.hjz.storyapp.data.pref.UserPreference
import com.hjz.storyapp.data.pref.dataStore
import com.hjz.storyapp.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfigStory.getApiService(user.token)
        return UserRepository.getInstance(apiService, pref)
    }
}