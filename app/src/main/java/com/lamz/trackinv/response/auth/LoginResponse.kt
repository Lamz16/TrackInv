package com.lamz.trackinv.response.auth

import com.google.gson.annotations.SerializedName

// TODO login auth is Done
data class LoginResponse(

	@field:SerializedName("data")
	val dataLoginPost: DataLogin,

	@field:SerializedName("success")
	val success: Boolean
)

data class DataLogin(

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("token")
	val token: String
)

data class User(

	@field:SerializedName("namaToko")
	val namaToko: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("alamat")
	val alamat: String
)
