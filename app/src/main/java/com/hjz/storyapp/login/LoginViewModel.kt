package com.hjz.storyapp.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.data.repository.UserRepository
import com.hjz.storyapp.data.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: UserRepository) : ViewModel() {
    val loginRespon : LiveData<LoginResponse> = repository.loginResponse
    val errorMessage : LiveData<String?> = repository.errorMessage
    val isLoading : LiveData<Boolean> = repository.isLoading


    fun clearErrorMessage() {
        repository.clearErrorMessage()
    }

    fun setLogin(email : String, password : String){
        viewModelScope.launch {
            repository.setLogin(email, password)
        }
    }

    fun saveSession(user: UserLogin) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}