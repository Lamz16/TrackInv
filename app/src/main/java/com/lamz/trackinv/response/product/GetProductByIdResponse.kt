package com.lamz.trackinv.response.product

import com.google.gson.annotations.SerializedName

data class GetProductByIdResponse(

    @field:SerializedName("name")
	val name: String,

    @field:SerializedName("id")
	val id: String,

    @field:SerializedName("hargaJual")
	val hargaJual: Int,

    @field:SerializedName("stok")
	val stok: Int,

    @field:SerializedName("category")
	val category: CategoryById,

    @field:SerializedName("hargaBeli")
	val hargaBeli: Int
)

data class CategoryById(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String
)
