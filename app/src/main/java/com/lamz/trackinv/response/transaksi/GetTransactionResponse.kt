package com.lamz.trackinv.response.transaksi

import com.google.gson.annotations.SerializedName

data class GetTransactionResponse(

	@field:SerializedName("data")
	val data: List<DataItemTransaction>,

	@field:SerializedName("success")
	val success: Boolean
)

data class Partner(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("type")
	val type: String
)

data class DataItemTransaction(

    @field:SerializedName("createdAt")
	val createdAt: String,

    @field:SerializedName("partner")
	val partner: Partner,

    @field:SerializedName("totalHarga")
	val totalHarga: Int,

    @field:SerializedName("id")
	val id: String,

    @field:SerializedName("type")
	val type: String
)
