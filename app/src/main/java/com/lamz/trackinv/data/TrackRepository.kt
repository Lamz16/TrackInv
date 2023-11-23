package com.lamz.trackinv.data

import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.lamz.trackinv.api.ApiConfig
import com.lamz.trackinv.api.ApiService
import com.lamz.trackinv.data.pref.UserModel
import com.lamz.trackinv.data.pref.UserPreference
import com.lamz.trackinv.helper.UiState
import com.lamz.trackinv.response.auth.LoginResponse
import com.lamz.trackinv.response.auth.RegisterResponse
import com.lamz.trackinv.response.category.AddCategoryResponse
import com.lamz.trackinv.response.category.GetAllCategoryResponse
import com.lamz.trackinv.response.category.GetCategoryIdResponse
import com.lamz.trackinv.response.product.AddProductResponse
import com.lamz.trackinv.response.product.GetProductResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class TrackRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun registerAccount(email: String, password: String, username : String, namaToko : String, alamat : String) = liveData {
        emit(UiState.Loading)
        try {
            val successResponse = apiService.register(email,password,username,namaToko, alamat)
            emit(UiState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(UiState.Error(errorResponse.toString()))
        } catch (e: Exception) {
            emit(UiState.Error("Error : ${e.message.toString()}"))
        }

    }

    suspend fun login(email: String, password: String) = liveData {
        emit(UiState.Loading)
        try {
            val successResponse = apiService.login(email,password)
            emit(UiState.Success(successResponse))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(UiState.Error(errorResponse.toString()))
        } catch (e: Exception) {
            emit(UiState.Error("Error : ${e.message.toString()}"))
        }
    }

    suspend fun addCategory(category : String) = liveData {
        emit(UiState.Loading)
        try {
            userPreference.getSession()
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.addCategory(category)
            emit(UiState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddCategoryResponse::class.java)
            emit(UiState.Error(errorResponse.toString()))
        } catch (e: Exception) {
            emit(UiState.Error("Error : ${e.message.toString()}"))
        }

    }

    suspend fun getCategory() = liveData {
        emit(UiState.Loading)
        try {
            userPreference.getSession()
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.getAllCategory()
            emit(UiState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GetAllCategoryResponse::class.java)
            emit(UiState.Error(errorResponse.toString()))
        } catch (e: Exception) {
            emit(UiState.Error("Error : ${e.message.toString()}"))
        }

    }

    suspend fun addProduct( name : String,stock : String,category : String,hargabeli : Int,hargaJual : Int,) = liveData {
        emit(UiState.Loading)
        try {
            userPreference.getSession()
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.addProduct(name, stock, category, hargabeli, hargaJual)
            emit(UiState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, AddProductResponse::class.java)
            emit(UiState.Error(errorResponse.toString()))
        } catch (e: Exception) {
            emit(UiState.Error("Error : ${e.message.toString()}"))
        }

    }

    suspend fun getCategoryId(id : String) = liveData {
        emit(UiState.Loading)
        try {
            userPreference.getSession()
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.getCategoryId(id)
            emit(UiState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GetCategoryIdResponse::class.java)
            emit(UiState.Error(errorResponse.toString()))
        } catch (e: Exception) {
            emit(UiState.Error("Error : ${e.message.toString()}"))
        }

    }

    suspend fun getProduct() = liveData {
        emit(UiState.Loading)
        try {
            userPreference.getSession()
            val user = runBlocking { userPreference.getSession().first() }
            val apiService = ApiConfig.getApiService(user.token)
            val successResponse = apiService.getAllProduct()
            emit(UiState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, GetProductResponse::class.java)
            emit(UiState.Error(errorResponse.toString()))
        } catch (e: Exception) {
            emit(UiState.Error("Error : ${e.message.toString()}"))
        }

    }

    companion object {
        @Volatile
        private var instance: TrackRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): TrackRepository =
            instance ?: synchronized(this) {
                instance ?: TrackRepository(userPreference, apiService)
            }.also { instance = it }
    }
}