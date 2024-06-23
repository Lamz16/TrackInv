package com.lamz.trackinv.utils

import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {
    val dbBarang = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").reference.child("data_barang")
    val dbTransaksi = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").reference.child("data_transaksi")
    val dbCustomer = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").reference.child("data_customer")
    val dbSupplier = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").reference.child("data_supplier")

}