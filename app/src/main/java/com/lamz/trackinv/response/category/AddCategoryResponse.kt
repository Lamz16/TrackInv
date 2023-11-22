package com.lamz.trackinv.response.category

import com.google.gson.annotations.SerializedName

data class AddCategoryResponse(

	@field:SerializedName("name")
	val name: String
)
