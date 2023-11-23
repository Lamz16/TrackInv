package com.lamz.trackinv.response.product

import com.google.gson.annotations.SerializedName

data class GetProductResponse(

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

	@field:SerializedName("hargaJual")
	val hargaJual: Int,

	@field:SerializedName("stock")
	val stok: Int,

	@field:SerializedName("category")
	val category: Category,

	@field:SerializedName("hargaBeli")
	val hargaBeli: Int
)

data class Category(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
