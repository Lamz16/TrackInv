package com.lamz.trackinv.domain.usecase

import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.model.CustomerModel
import com.lamz.trackinv.domain.model.StockData
import com.lamz.trackinv.domain.model.SupplierModel
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.domain.repository.TrackRepository
import com.lamz.trackinv.presentation.ui.state.UiState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackUseCaseInteract @Inject constructor(
    private val repository: TrackRepository
): TrackInvUseCase {
    override suspend fun addSupplier(supplier: SupplierModel) {
        repository.addSupplier(supplier)
    }

    override fun getAllSupplier(): Flow<List<SupplierModel>> {
        return repository.getAllSupplier()
    }

    override fun getSupplierById(idSupplier: String): Flow<SupplierModel> {
        return repository.getSupplierById(idSupplier)
    }

    override suspend fun addCustomer(customer: CustomerModel) {
        repository.addCustomer(customer)
    }

    override fun getAllCustomer(): Flow<List<CustomerModel>> {
        return repository.getAllCustomer()
    }

    override fun getCustomerById(idCustomer: String): Flow<CustomerModel> {
        return repository.getCustomerById(idCustomer)
    }

    override suspend fun addProduct(barang: BarangModel) {
        repository.addProduct(barang)
    }

    override fun getAllProduct(): Flow<List<BarangModel>> {
        return repository.getAllProduct()
    }

    override fun getProductId(idBarang: String): Flow<BarangModel> {
        return repository.getProductId(idBarang)
    }

    override suspend fun deleteProduct(idBarang: String) {
        repository.deleteProduct(idBarang)
    }

    override suspend fun updateProduct(idBarang: String, barang: BarangModel) {
        repository.updateProduct(idBarang, barang)
    }

    override suspend fun updateStock(idBarang: String, newStock: Int) {
       repository.updateStock(idBarang,newStock)
    }

    override suspend fun addTransactionStock(transaksi: TransaksiModel) {
        repository.addTransactionStock(transaksi)
    }

    override fun getAllTransaction(): Flow<UiState<List<TransaksiModel>>> {
        return repository.getAllTransaction()
    }

    override fun getAllUpdatedTransaction(): Flow<UiState<List<TransaksiModel>>> {
        return repository.getAllUpdatedTransaction()
    }

    override fun getTransactionsByDateRange(
        fromDate: String,
        toDate: String,
        namaBarang: String
    ): Flow<List<TransaksiModel>> = repository.getTransactionsByDateRange(fromDate, toDate,namaBarang)

    override fun predictStockOut(
        fromDate: String,
        toDate: String,
        namaBarang: String
    ): Flow<Triple<List<StockData>, Double, Double>> = repository.predictStockOut(fromDate, toDate, namaBarang)


}