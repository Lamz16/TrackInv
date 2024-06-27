package com.lamz.trackinv.data.repositories

import com.lamz.trackinv.domain.model.BarangModel
import com.lamz.trackinv.domain.model.CustomerModel
import com.lamz.trackinv.domain.model.StockData
import com.lamz.trackinv.domain.model.SupplierModel
import com.lamz.trackinv.domain.model.TransaksiModel
import com.lamz.trackinv.domain.repository.TrackRepository
import com.lamz.trackinv.presentation.ui.state.UiState
import com.lamz.trackinv.utils.DoubleExponentialSmoothing
import com.lamz.trackinv.utils.FirebaseUtils
import com.lamz.trackinv.utils.getDatesBetween
import com.lamz.trackinv.utils.getFormattedCurrentDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackRepositoryImpl @Inject constructor() : TrackRepository {
        override suspend fun addSupplier(supplier: SupplierModel) {
        val supplierId = supplier.idSupp ?: ""
        val supplierRef = FirebaseUtils.dbSupplier.child(supplierId)
        supplierRef.setValue(supplier).await()
    }

    override fun getAllSupplier(): Flow<List<SupplierModel>> = flow{
        val snapshot = FirebaseUtils.dbSupplier.get().await()
        val supplierList = snapshot.children.mapNotNull { it.getValue(SupplierModel::class.java) }
        emit(supplierList)
    }

    override fun getSupplierById(idSupplier: String): Flow<SupplierModel> = flow {
        val snapshot = FirebaseUtils.dbSupplier.orderByChild("idSupplier").equalTo(idSupplier).get().await()
        val supplier = snapshot.children.firstOrNull()?.getValue(SupplierModel::class.java)
        emit(supplier ?: SupplierModel())
    }

    override suspend fun addCustomer(customer: CustomerModel) {
        val customerId = customer.idCs ?:""
        val customerRef = FirebaseUtils.dbCustomer.child(customerId)
        customerRef.setValue(customer).await()
    }

    override fun getAllCustomer(): Flow<List<CustomerModel>> = flow{
        val snapshot = FirebaseUtils.dbCustomer.get().await()
        val customerList = snapshot.children.mapNotNull { it.getValue(CustomerModel::class.java) }
        emit(customerList)
    }

    override  fun getCustomerById(idCustomer: String): Flow<CustomerModel> = flow {
        val snapshot = FirebaseUtils.dbCustomer.orderByChild("idCustomer").equalTo(idCustomer).get().await()
        val customer = snapshot.children.firstOrNull()?.getValue(CustomerModel::class.java)
        emit(customer ?: CustomerModel())
    }

    override suspend fun addProduct(barang: BarangModel) {
        val barangId = barang.idBarang ?: ""
        val barangRef = FirebaseUtils.dbBarang.child(barangId)
        barangRef.setValue(barang).await()
    }

    override  fun getAllProduct(): Flow<List<BarangModel>> = flow {
        val snapshot = FirebaseUtils.dbBarang.get().await()
        val productList = snapshot.children.mapNotNull { it.getValue(BarangModel::class.java) }
        emit(productList)
    }

    override fun getProductId(idBarang: String): Flow<BarangModel> = flow{
        val snapshot = FirebaseUtils.dbBarang.orderByChild("idBarang").equalTo(idBarang).get().await()
        val barang = snapshot.children.firstOrNull()?.getValue(BarangModel::class.java)
        emit(barang ?: BarangModel())
    }

    override fun getAllProductMoreThan(): Flow<List<BarangModel>> = flow {
        val snapshot = FirebaseUtils.dbBarang.orderByChild("stokBarang").startAt(50.0).get().await()
        val productList = snapshot.children.mapNotNull { it.getValue(BarangModel::class.java) }
        emit(productList)
    }

    override fun getAllProductThin(): Flow<List<BarangModel>> = flow {
        val snapshot = FirebaseUtils.dbBarang.orderByChild("stokBarang").startAt(1.0).endAt(49.0).get().await()
        val productList = snapshot.children.mapNotNull { it.getValue(BarangModel::class.java) }
        emit(productList)
    }

    override fun getALlProductOut(): Flow<List<BarangModel>> = flow{
        val snapshot = FirebaseUtils.dbBarang.orderByChild("stokBarang").equalTo(0.0).get().await()
        val productList = snapshot.children.mapNotNull { it.getValue(BarangModel::class.java) }
        emit(productList)
    }

    override suspend fun deleteProduct(idBarang: String) {
        val barangRef = FirebaseUtils.dbBarang.child(idBarang)
        barangRef.removeValue().await()
    }

    override suspend fun updateProduct(idBarang: String, barang : BarangModel) {
        val barangRef = FirebaseUtils.dbBarang.child(idBarang)
        barangRef.setValue(barang).await()
    }

    override suspend fun updateStock(idBarang: String, newStock: Int) {
        val ref = FirebaseUtils.dbBarang.child(idBarang).child("stokBarang")
        ref.setValue(newStock)
    }

    override suspend fun addTransactionStock(transaksi: TransaksiModel) {
        val transaksiId = FirebaseUtils.dbTransaksi.push().key!!
        val transaksiRef = FirebaseUtils.dbTransaksi.child(transaksiId)
        transaksiRef.setValue(transaksi).await()
    }

    override fun getAllTransaction(): Flow<UiState<List<TransaksiModel>>> = flow {
        emit(UiState.Loading)
        try {
            val snapshot = FirebaseUtils.dbTransaksi.get().await()
            val transactionList = snapshot.children.mapNotNull { it.getValue(TransaksiModel::class.java) }
            emit(UiState.Success(transactionList))
        }catch (e : Exception){
            emit(UiState.Error(e.message.toString()))
        }

    }.flowOn(Dispatchers.IO)

    override fun getAllUpdatedTransaction(): Flow<UiState<List<TransaksiModel>>> = flow {
        emit(UiState.Loading)
        try {
            val currentDate = getFormattedCurrentDate()
            val snapshot = FirebaseUtils.dbTransaksi.orderByChild("tglTran").equalTo(currentDate).get().await()
            val transactionList = snapshot.children.mapNotNull { it.getValue(TransaksiModel::class.java) }
            emit(UiState.Success(transactionList))
        }catch (e : Exception){
            emit(UiState.Error(e.message.toString()))
        }
    }.flowOn(Dispatchers.IO)

    override fun getTransactionsByDateRange(fromDate: String, toDate: String, namaBarang: String): Flow<List<TransaksiModel>> = flow {
        val snapshot = FirebaseUtils.dbTransaksi.get().await()
        val transactionList = snapshot.children.mapNotNull { it.getValue(TransaksiModel::class.java) }
        val filteredTransactions = transactionList.filter {
            val transactionDate = SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(it.tglTran!!)?.time ?: 0
            val fromDateMillis = SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(fromDate)?.time ?: 0
            val toDateMillis = SimpleDateFormat("dd-MM-yyyy", Locale.US).parse(toDate)?.time ?: 0
            it.namaBarang == namaBarang && transactionDate in fromDateMillis..toDateMillis
        }
        emit(filteredTransactions)
    }

    override fun predictStockOut(
        fromDate: String,
        toDate: String,
        namaBarang: String
    ): Flow<Triple<List<StockData>, Double, Double>> = flow{
        val transactions = getTransactionsByDateRange(fromDate, toDate, namaBarang).firstOrNull()

        if (transactions != null) {
            val stockOutData = mutableListOf<StockData>()
            val dateRange = getDatesBetween(fromDate, toDate)

            for (date in dateRange) {
                val transaction = transactions.find { it.tglTran == date && it.jenisTran == "Keluar" }
                val stock = transaction?.jumlah?.toDoubleOrNull() ?: 0.0
                stockOutData.add(StockData(date, stock))
            }

            val des = DoubleExponentialSmoothing(0.5, 0.3)
            val prediction = des.predict(stockOutData, stockOutData.size)

            val mape = des.calculateMAPE(stockOutData, prediction)
            val mse = des.calculateMSE(stockOutData, prediction)
            emit(Triple(prediction, mape, mse))
        } else {
            emit(Triple(emptyList(), 0.0, 0.0)) // Return empty list if no transactions found
        }
    }
}