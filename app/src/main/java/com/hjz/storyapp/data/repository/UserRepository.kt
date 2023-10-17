package com.hjz.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.hjz.storyapp.data.api.ApiConfig
import com.hjz.storyapp.data.api.ApiConfigStory
import com.hjz.storyapp.data.api.ApiService
import com.hjz.storyapp.data.pref.UserLogin
import com.hjz.storyapp.data.pref.UserPreference
import com.hjz.storyapp.data.response.ErrorResponse
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.data.response.LoginResponse
import com.hjz.storyapp.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference) {

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?>
        get() = _successMessage

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> get() = _isLoading


    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess : LiveData<Boolean> get() = _isSuccess

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> get() = _loginResponse


    // Metode untuk menghapus pesan sukses dan pesan kesalahan
    fun clearSuccessMessage() {
        _successMessage.value = null
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    /*
    Repository For Signup
    * */
    suspend fun register(name: String, email: String, password: String) {
        _isLoading.value = false
        try {
            _isLoading.value = true
            // Registrasi berhasil
            val repository = ApiConfig.getApiService()
            val message = repository.register(name, email, password).message
            _successMessage.value = message
            _isLoading.value = false
            Log.d("Signupviewmodel", "Sukses")

        } catch (e: HttpException) {
            _isLoading.value = true
            // Registrasi gagal
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            _errorMessage.value = errorMessage
            _isLoading.value = false
            Log.d("Signupviewmodel", "Gagal")
            Log.d("namahenry", "nama = $name, email = $email, password $password")
        }
    }

    fun setLogin(email : String, password: String){
        _isLoading.value = false
        _isLoading.value = true
        ApiConfig.getApiService().setLogin(email, password)
            .enqueue(object : Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful && response.body() != null){
                        _loginResponse.value = response.body()
                        _isLoading.value = false
                    } else {
                        _errorMessage.value = response.message()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = true
                    _errorMessage.value = t.message
                    Log.d("User Login", " onFailure : ${t.message}")
                }
            })
    }

    suspend fun saveSession(user: UserLogin) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserLogin> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }


    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService : ApiService,
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userPreference)
            }.also { instance = it }
    }
}