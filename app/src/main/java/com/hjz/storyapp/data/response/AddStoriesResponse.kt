package com.hjz.storyapp.data.response

import com.google.gson.annotations.SerializedName

data class AddStoriesResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
