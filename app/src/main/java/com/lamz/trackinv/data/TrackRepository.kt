package com.lamz.trackinv.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lamz.trackinv.data.model.AuthModel
import com.lamz.trackinv.data.model.BarangModel
import com.lamz.trackinv.data.model.CustomerModel
import com.lamz.trackinv.data.model.SupplierModel
import com.lamz.trackinv.data.model.TransaksiModel
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.data.pref.UserPreference
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.utils.FirebaseUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TrackRepository(
    private val userPreference: UserPreference,
) : TrackRepositoryImpl {

    private val database =
        FirebaseDatabase.getInstance("https://track-inventory-app-default-rtdb.firebaseio.com/")
    private val dbReferenceAuth = database.reference.child("data_users")


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    override suspend fun login(email: String, password: String): Flow<UiState<AuthModel>> = flow {
        emit(UiState.Loading)
        try {
            val authModel = withContext(Dispatchers.IO) {
                suspendCoroutine { continuation ->
                    dbReferenceAuth.orderByChild("email").equalTo(email)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (account in snapshot.children) {
                                        val dataAccount = account.getValue(AuthModel::class.java)
                                        if (dataAccount != null && dataAccount.password == password) {
                                            val authModel = AuthModel(
                                                dataAccount.idUser,
                                                dataAccount.nama,
                                                dataAccount.email,
                                                dataAccount.password,
                                                dataAccount.username
                                            )
                                            continuation.resume(authModel)
                                            return
                                        }
                                    }
                                }
                                continuation.resume(null)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                continuation.resumeWithException(error.toException())
                            }
                        })
                }
            }
            if (authModel != null) {
                emit(UiState.Success(authModel))
            } else {
                emit(UiState.Error("User Not Found or Incorrect Password"))
            }
        } catch (e: Exception) {
            emit(UiState.Error(e.message ?: "Unknown Error"))
        }
    }

    override suspend fun register(register: AuthModel) {
        val userId = dbReferenceAuth.push().key!!
        val userRef = dbReferenceAuth.child(userId)
        userRef.setValue(register).await()
    }

    override suspend fun addSupplier(supplier: SupplierModel) {
        val supplierId = supplier.idSupp ?: ""
        val supplierRef = FirebaseUtils.dbSupplier.child(supplierId)
        supplierRef.setValue(supplier).await()
    }

    override suspend fun getAllSupplier(idUser : String): Flow<List<SupplierModel>> {
        val snapshot = FirebaseUtils.dbSupplier.orderByChild("idUser").equalTo(idUser).get().await()
        return flowOf(snapshot.children.mapNotNull { it.getValue(SupplierModel::class.java) })
    }

    override suspend fun getSupplierById(idSupplier: String): Flow<SupplierModel> {
        val snapshot = FirebaseUtils.dbSupplier.orderByChild("idSupplier").equalTo(idSupplier).get().await()
        val supplier = snapshot.children.firstOrNull()?.getValue(SupplierModel::class.java)
        return  flowOf(supplier ?:SupplierModel())
    }

    override suspend fun addCustomer(customer: CustomerModel) {
        val customerId = customer.idCs ?:""
        val customerRef = FirebaseUtils.dbCustomer.child(customerId)
        customerRef.setValue(customer).await()
    }

    override suspend fun getAllCustomer(idUser : String): Flow<List<CustomerModel>> {
        val snapshot = FirebaseUtils.dbCustomer.orderByChild("idUser").equalTo(idUser).get().await()
        return flowOf(snapshot.children.mapNotNull { it.getValue(CustomerModel::class.java) })
    }

    override suspend fun getCustomerById(idCustomer: String): Flow<CustomerModel> {
        val snapshot = FirebaseUtils.dbCustomer.orderByChild("idCustomer").equalTo(idCustomer).get().await()
        val customer = snapshot.children.firstOrNull()?.getValue(CustomerModel::class.java)
        return  flowOf(customer ?:CustomerModel())
    }

    override suspend fun addProduct(barang: BarangModel) {
        val barangId = barang.idBarang ?: ""
        val barangRef = FirebaseUtils.dbBarang.child(barangId)
        barangRef.setValue(barang).await()
    }

    override suspend fun getAllProduct(idUser: String): Flow<List<BarangModel>> {
        val snapshot = FirebaseUtils.dbBarang.orderByChild("idUser").equalTo(idUser).get().await()
        return flowOf(snapshot.children.mapNotNull { it.getValue(BarangModel::class.java) })
    }

    override suspend fun getProductId(idBarang: String): Flow<BarangModel> {
        val snapshot = FirebaseUtils.dbBarang.orderByChild("idBarang").equalTo(idBarang).get().await()
        val barang = snapshot.children.firstOrNull()?.getValue(BarangModel::class.java)
        return  flowOf(barang ?:BarangModel())
    }

    override suspend fun deleteProduct(idBarang: String) {
        val barangRef = FirebaseUtils.dbBarang.child(idBarang)
        barangRef.removeValue().await()
    }

    override suspend fun updateProduct(idBarang: String, barang : BarangModel) {
        val barangRef = FirebaseUtils.dbBarang.child(idBarang)
        barangRef.setValue(barang).await()
    }

    override suspend fun addTransactionStock(transaksi: TransaksiModel) {
        val transaksiId = FirebaseUtils.dbTransaksi.push().key!!
        val transaksiRef = FirebaseUtils.dbTransaksi.child(transaksiId)
        transaksiRef.setValue(transaksi).await()
    }
}