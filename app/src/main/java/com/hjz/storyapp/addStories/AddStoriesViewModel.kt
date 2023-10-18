package com.hjz.storyapp.addStories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.data.repository.UserRepository

class AddStoriesViewModel (private val repository: UserRepository) : ViewModel() {

    var getToken : String? = null
    fun setToken(token : String){
        getToken = token
    }

    fun getSession(): LiveData<UserLogin> {
        return repository.getSession().asLiveData()
    }
}