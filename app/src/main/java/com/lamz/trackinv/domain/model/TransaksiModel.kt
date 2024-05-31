package com.lamz.trackinv.domain.model

data class TransaksiModel(
    val idTransaksi : String? = "",
    val jenisTran : String? = "",
    val namaPartner : String? = "",
    val namaBarang : String? = "",
    val nominal : String? = "",
    val tglTran : String? =""
)
