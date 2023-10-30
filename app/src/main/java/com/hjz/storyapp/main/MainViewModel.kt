package com.hjz.storyapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.data.repository.UserRepository
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MainViewModel (private val repository: UserRepository) : ViewModel(){

    fun getSession(): LiveData<UserLogin> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val stories: LiveData<PagingData<ListStoryItem>> =
        repository.getStory().cachedIn(viewModelScope)

    val isLoading : LiveData<Boolean> = repository.isLoading
}