package com.hjz.storyapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.data.repository.UserRepository
import com.hjz.storyapp.data.response.ListStoryItem

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    val isLoading : LiveData<Boolean> = repository.isLoading
    val mapStory : LiveData<List<ListStoryItem>> = repository.mapStory

    fun getStoriesWithLocation(token : String) {
        repository.getStoriesWithLocation(token)
    }

    fun getSession(): LiveData<UserLogin> {
        return repository.getSession().asLiveData()
    }
}