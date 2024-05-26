package com.lamz.trackinv.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BarangModel(
    val idBarang : String? = "",
    val idUser : String? = "",
    val namaBarang: String? = "",
    val stokBarang : String? = "",
    val buy : String? = "",
    val sell : String? =""
) : Parcelable
