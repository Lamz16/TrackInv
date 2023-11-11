package com.lamz.trackinv.data.pref

data class UserModel(
    val username : String,
    val password : String,
    val isLogin : Boolean = false
)