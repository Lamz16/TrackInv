package com.lamz.trackinv.response.partner

import com.google.gson.annotations.SerializedName

data class GetCustomerResponse(

	@field:SerializedName("data")
	val data: List<DataItemCustomer>,

	@field:SerializedName("success")
	val success: Boolean
)

data class DataItemCustomer(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("userId")
	val userId: String
)
