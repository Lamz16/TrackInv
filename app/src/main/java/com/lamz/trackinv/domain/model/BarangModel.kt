package com.lamz.trackinv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarangModel(
    val idBarang : String? = "",
    val namaBarang: String? = "",
    val stokBarang : Int? = 0,
    val buy : String? = "",
    val sell : String? =""
) : Parcelable
