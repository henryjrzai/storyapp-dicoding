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


    private val _isLoading = MutableLiveData<Boolean> ()
    val isLoading : LiveData<Boolean> = _isLoading



    companion object {

    }
}