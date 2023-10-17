package com.hjz.storyapp.signup

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.hjz.storyapp.data.api.ApiConfig
import com.hjz.storyapp.data.repository.UserRepository
import com.hjz.storyapp.data.response.ErrorResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel (private val repository: UserRepository) : ViewModel() {
    val successMessage : LiveData<String?> = repository.successMessage
    val errorMessage : LiveData<String?> = repository.errorMessage
    val isLoading : LiveData<Boolean> = repository.isLoading

    fun clearSuccessMessage() {
        repository.clearSuccessMessage()
    }

    fun clearErrorMessage() {
        repository.clearErrorMessage()
    }

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            repository.register(name, email, password)
        }
    }
}