package com.hjz.storyapp.data.pref

data class UserLogin(
    var name: String,
    var token: String,
    var userId : String,
    var isLogin: Boolean = false
)
