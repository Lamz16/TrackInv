package com.lamz.trackinv.response.auth

import com.google.gson.annotations.SerializedName


// TODO 2 register auth is Done
data class RegisterResponse(

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("namaToko")
	val namaToko: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String,

	@field:SerializedName("alamat")
	val alamat: String
)
