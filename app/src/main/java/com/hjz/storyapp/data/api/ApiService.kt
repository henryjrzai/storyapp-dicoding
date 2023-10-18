package com.hjz.storyapp.data.api

import com.hjz.storyapp.data.response.AddStoriesResponse
import com.hjz.storyapp.data.response.DetailStoryResponse
import com.hjz.storyapp.data.response.LoginResponse
import com.hjz.storyapp.data.response.RegisterResponse
import com.hjz.storyapp.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    fun setLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getStories(): Call<StoryResponse>

    @GET("stories/{id}")
    fun getDetailStories(
        @Path("id") id : String
    ) : Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    fun addStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : Call<AddStoriesResponse>
}