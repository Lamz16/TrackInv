package com.lamz.trackinv.response.category

import com.google.gson.annotations.SerializedName

data class GetAllCategoryResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("success")
	val success: Boolean
)

data class DataItem(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
