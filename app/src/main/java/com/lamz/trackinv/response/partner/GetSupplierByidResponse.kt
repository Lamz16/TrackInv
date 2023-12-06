package com.lamz.trackinv.response.partner

import com.google.gson.annotations.SerializedName

data class GetSupplierByidResponse(

	@field:SerializedName("data")
	val data: DataSupplier,

	@field:SerializedName("success")
	val success: Boolean
)

data class DataSupplier(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
