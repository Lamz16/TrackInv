package com.lamz.trackinv.response.membership

import com.google.gson.annotations.SerializedName

data class MembershipResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean
)

data class Data(

	@field:SerializedName("redirect_url")
	val redirectUrl: String,

	@field:SerializedName("token")
	val token: String
)
