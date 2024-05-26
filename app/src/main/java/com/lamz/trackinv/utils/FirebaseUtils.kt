package com.lamz.trackinv.utils

import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {

 val dbUser = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").getReference("data_users")
    val dbCategory = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").getReference("data_kategori")
    val dbBarang = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").getReference("data_barang")
    val dbTransaksi = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").getReference("data_transaksi")
    val dbCustomer = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").getReference("data_customer")
    val dbSupplier = FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/").getReference("data_supplier")

}