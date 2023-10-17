package com.hjz.storyapp.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hjz.storyapp.data.api.ApiConfigStory
import com.hjz.storyapp.data.response.ListStoryItem
import com.hjz.storyapp.data.response.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel : ViewModel() {
    private val _listStory = MutableLiveData<List<ListStoryItem>> ()
    val listStory : LiveData<List<ListStoryItem>> = _listStory

    private val _isLoading = MutableLiveData<Boolean> ()
    val isLoading : LiveData<Boolean> = _isLoading

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
                } else {
                    Log.d(STORY_MODEL, response.message())
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("UserViewModel", "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private val STORY_MODEL = "story_model"
    }
}