package com.lamz.trackinv.response.transaksi

import com.google.gson.annotations.SerializedName

data class InComingResponse(

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("partner")
	val partner: PartnerIncom,

	@field:SerializedName("totalHarga")
	val totalHarga: Int,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("type")
	val type: String
)

data class PartnerIncom(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("type")
	val type: String
)
