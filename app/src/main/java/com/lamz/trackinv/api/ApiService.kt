package com.lamz.trackinv.api

import com.lamz.trackinv.data.OutGoing
import com.lamz.trackinv.response.auth.LoginResponse
import com.lamz.trackinv.response.auth.RegisterResponse
import com.lamz.trackinv.response.category.AddCategoryResponse
import com.lamz.trackinv.response.category.GetAllCategoryResponse
import com.lamz.trackinv.response.category.GetCategoryIdResponse
import com.lamz.trackinv.response.product.AddProductResponse
import com.lamz.trackinv.response.product.DeleteProductResponse
import com.lamz.trackinv.response.product.GetProductByIdResponse
import com.lamz.trackinv.response.product.GetProductResponse
import com.lamz.trackinv.response.product.UpdateProductResponse
import com.lamz.trackinv.response.transaksi.OutgoingResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("categories/{id}")
    suspend fun getCategoryId(
        @Path("id") id: String
    ): GetCategoryIdResponse


    @FormUrlEncoded
    @POST("products")
    suspend fun addProduct(
        @Field("name") name : String,
        @Field("stock") stock : String,
        @Field("categoryId") category : String,
        @Field("hargaBeli") hargabeli : Int,
        @Field("hargaJual") hargaJual : Int,
    ) : AddProductResponse

    @GET("products")
    suspend fun getAllProduct(): GetProductResponse

    @GET("products/{id}")
    suspend fun getProductId(
        @Path("id") id: String
    ): GetProductByIdResponse

    @DELETE("products/{id}")
    suspend fun deleteProdct(
        @Path("id") id: String
    ): DeleteProductResponse

    @FormUrlEncoded
    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id : String,
        @Field("name") name: String,
        @Field("stock") stock: String,
        @Field("categoryId") category: String,
        @Field("hargaBeli") hargabeli: Int,
        @Field("hargaJual") hargaJual: Int,
    ): UpdateProductResponse

    @POST("transactions/outgoing")
    @Headers("Content-Type: application/json")
    suspend fun outGoingTran(
        @Query("partnerId") partnerId: String,
        @Body items: OutGoing
    ): OutgoingResponse

    @POST("transactions/incoming")
    @Headers("Content-Type: application/json")
    suspend fun incomingTran(
        @Query("partnerId") partnerId: String,
        @Body items: OutGoing
    ): OutgoingResponse

}