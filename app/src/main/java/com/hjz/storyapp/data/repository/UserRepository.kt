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
import com.hjz.storyapp.data.response.AddStoriesResponse
import com.hjz.storyapp.data.response.DetailStoryResponse
import com.hjz.storyapp.data.response.ErrorResponse
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.data.response.LoginResponse
import com.hjz.storyapp.data.response.Story
import com.hjz.storyapp.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

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
    val isLoading : LiveData<Boolean> = _isLoading


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

    // List Stories
    private val _listStory = MutableLiveData<List<ListStoryItem>> ()
    val listStory : LiveData<List<ListStoryItem>> = _listStory
    fun getListStory(token : String) {
        _isLoading.value = true
        ApiConfigStory.getApiService(token).getStories().enqueue(object : Callback<StoryResponse>{
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    _listStory.postValue(response.body()?.listStory)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("UserViewModel", "onFailure: ${t.message}")
            }

        })
    }

    // Detail Stories
    private val _detailStories = MutableLiveData<Story>()
    val detailStories: LiveData<Story> = _detailStories
    fun setDetailStories(token: String, id : String){
        _isLoading.value = true
        ApiConfigStory.getApiService(token).getDetailStories(id)
            .enqueue(object : Callback<DetailStoryResponse>{
                override fun onResponse(
                    call: Call<DetailStoryResponse>,
                    response: Response<DetailStoryResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful){
                        _detailStories.postValue(response.body()?.story)
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    _isLoading.value = true
                    Log.d("DetailRepo", "onFailure : ${t.message}")
                }
            })
    }

    fun addStories(token : String, imageFile : File, description : String){
        _isLoading.value = true
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        ApiConfigStory.getApiService(token).addStories(multipartBody, requestBody)
            .enqueue(object : Callback<AddStoriesResponse>{
                override fun onResponse(
                    call: Call<AddStoriesResponse>,
                    response: Response<AddStoriesResponse>
                ) {
                    if (response.isSuccessful){
                        _isLoading.value = false
                        _successMessage.value = response.message().toString()
                    } else {
                        _errorMessage.value = response.errorBody().toString()
                    }
                }

                override fun onFailure(call: Call<AddStoriesResponse>, t: Throwable) {
                    _errorMessage.value = t.message
                }
            })
    }

    // Story Maps
    private val _mapStory = MutableLiveData<List<ListStoryItem>> ()
    val mapStory : LiveData<List<ListStoryItem>> = _mapStory
    fun getStoriesWithLocation(token : String) {
        _isLoading.value = true
        ApiConfigStory.getApiService(token).getStoriesWithLocation()
            .enqueue(object : Callback<StoryResponse>{
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = false
                        _mapStory.postValue(response.body()?.listStory)
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
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