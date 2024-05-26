package com.lamz.trackinv.data

import com.lamz.trackinv.data.model.AuthModel
import com.lamz.trackinv.data.model.BarangModel
import com.lamz.trackinv.data.model.CustomerModel
import com.lamz.trackinv.data.model.SupplierModel
import com.lamz.trackinv.helper.UiState
import kotlinx.coroutines.flow.Flow

interface TrackRepositoryImpl {
    suspend fun login(email: String, password: String): Flow<UiState<AuthModel>>
    suspend fun register(register: AuthModel)
    suspend fun addSupplier(supplier: SupplierModel)
    suspend fun getAllSupplier(idUser: String): Flow<List<SupplierModel>>
    suspend fun addCustomer(customer: CustomerModel)
    suspend fun getAllCustomer(idUser: String): Flow<List<CustomerModel>>
    suspend fun addProduct(barang: BarangModel)
    suspend fun getAllProduct(idUser: String): Flow<List<BarangModel>>
    suspend fun getProductId(idBarang: String): Flow<BarangModel>
}