package com.lamz.trackinv.response.transaksi

import com.google.gson.annotations.SerializedName

data class OutgoingResponse(

    @field:SerializedName("createdAt")
	val createdAt: String,

    @field:SerializedName("partner")
	val partner: PartnerOutcom,

    @field:SerializedName("totalHarga")
	val totalHarga: Int,

    @field:SerializedName("id")
	val id: String,

    @field:SerializedName("type")
	val type: String
)

data class PartnerOutcom(

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("type")
	val type: String
)
