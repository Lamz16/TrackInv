package com.lamz.trackinv.response.customer

import com.google.gson.annotations.SerializedName

data class CustomerResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean
)

data class DataItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("userId")
	val userId: String
)
