package com.lamz.trackinv.response.category

import com.google.gson.annotations.SerializedName

data class GetCategoryIdResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("success")
	val success: Boolean
)

data class Data(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
