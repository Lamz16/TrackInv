package com.lamz.trackinv.data.pref

data class UserModel(
    val email : String,
    val idUser : String,
    val name : String,
    val isLogin : Boolean = false
)