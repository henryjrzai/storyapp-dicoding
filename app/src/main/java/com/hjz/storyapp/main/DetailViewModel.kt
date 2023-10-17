package com.hjz.storyapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.data.repository.UserRepository
import com.hjz.storyapp.data.response.Story

class DetailViewModel (private val repository: UserRepository) : ViewModel() {
    val isLoading : LiveData<Boolean> = repository.isLoading
    val detailStories : LiveData<Story> = repository.detailStories

    fun setDetailStories(token : String, id : String) {
        repository.setDetailStories(token, id)
    }

    fun getSession(): LiveData<UserLogin> {
        return repository.getSession().asLiveData()
    }
}