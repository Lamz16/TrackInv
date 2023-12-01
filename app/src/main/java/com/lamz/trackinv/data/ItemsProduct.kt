package com.lamz.trackinv.data


data class OutGoing(
    val partnerId: String,
    val items: List<ItemsProduct>
)
data class ItemsProduct(
    val id : String,
    val qty : Int,
)