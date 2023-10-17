package com.hjz.storyapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.data.repository.UserRepository
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class MainViewModel (private val repository: UserRepository) : ViewModel(){

    fun getSession(): LiveData<UserLogin> {
        return repository.getSession().asLiveData()
    }

    val isLoading : LiveData<Boolean> = repository.isLoading
    val listStory : LiveData<List<ListStoryItem>> = repository.listStoryItem
    fun getAllStory(token : String){
        repository.getAllStory(token)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}