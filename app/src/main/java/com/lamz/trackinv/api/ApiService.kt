package com.lamz.trackinv.api

import com.lamz.trackinv.response.auth.LoginResponse
import com.lamz.trackinv.response.auth.RegisterResponse
import com.lamz.trackinv.response.category.AddCategoryResponse
import com.lamz.trackinv.response.category.GetAllCategoryResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("auth/register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("username") username: String,
        @Field("namaToko") namaToko: String,
        @Field("alamat") alamat: String,
    ) : RegisterResponse

    @FormUrlEncoded
    @POST("auth/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ) : LoginResponse

    @FormUrlEncoded
    @POST("categories")
    suspend fun addCategory(
        @Field("name") name : String
    ) : AddCategoryResponse

    @GET("categories")
    suspend fun getAllCategory(): GetAllCategoryResponse

    @FormUrlEncoded
    @POST("products")
    suspend fun addProduct(
        @Field("name") name : String,
        @Field("stock") stock : String,
        @Field("categoryId") category : String,
        @Field("hargaBeli") hargabeli : Int,
        @Field("hargaJual") hargaJual : Int,
    ) : AddCategoryResponse

}