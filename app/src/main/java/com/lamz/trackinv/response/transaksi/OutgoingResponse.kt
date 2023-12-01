package com.lamz.trackinv.response.transaksi

import com.google.gson.annotations.SerializedName

data class OutgoingResponse(

	@field:SerializedName("partnerId")
	val partnerId: String,

	@field:SerializedName("items")
	val items: List<ItemsItem>
)

data class ItemsItem(

	@field:SerializedName("qty")
	val qty: Int,

	@field:SerializedName("id")
	val id: String
)
