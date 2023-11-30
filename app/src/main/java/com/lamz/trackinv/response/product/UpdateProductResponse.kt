package com.lamz.trackinv.response.product

import com.google.gson.annotations.SerializedName

data class UpdateProductResponse(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("hargaJual")
	val hargaJual: Int,

	@field:SerializedName("stock")
	val stock: String,

	@field:SerializedName("hargaBeli")
	val hargaBeli: Int,

	@field:SerializedName("categoryId")
	val categoryId: String
)
