package com.hjz.storyapp.addStories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.data.repository.UserRepository
import java.io.File

class AddStoriesViewModel (private val repository: UserRepository) : ViewModel() {

    val isLoading : LiveData<Boolean> = repository.isLoading
    val successMessage : LiveData<String?> = repository.successMessage
    val errorMessage : LiveData<String?> = repository.errorMessage

    fun addStories(token : String, imageFile : File, description : String) {
        repository.addStories(token, imageFile, description)
    }

    fun getSession(): LiveData<UserLogin> {
        return repository.getSession().asLiveData()
    }
}