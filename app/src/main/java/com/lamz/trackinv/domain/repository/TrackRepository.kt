package com.lamz.trackinv.domain.repository

import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.model.CustomerModel
import com.lamz.trackinv.domain.model.SupplierModel
import com.lamz.trackinv.domain.model.TransaksiModel
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun addSupplier(supplier: SupplierModel)
    fun getAllSupplier(): Flow<List<SupplierModel>>
    fun getSupplierById(idSupplier: String): Flow<SupplierModel>
    suspend fun addCustomer(customer: CustomerModel)
    fun getAllCustomer(): Flow<List<CustomerModel>>
    fun getCustomerById(idCustomer: String): Flow<CustomerModel>
    suspend fun addProduct(barang: BarangModel)
    fun getAllProduct(): Flow<List<BarangModel>>
    fun getProductId(idBarang: String): Flow<BarangModel>
    suspend fun deleteProduct(idBarang : String)
    suspend fun updateProduct(idBarang : String, barang: BarangModel)
    suspend fun updateStock(idBarang: String, newStock: Int)
    suspend fun addTransactionStock(transaksi : TransaksiModel)

    fun getAllTransaction() : Flow<List<TransaksiModel>>
    fun getAllUpdatedTransaction() : Flow<List<TransaksiModel>>
}