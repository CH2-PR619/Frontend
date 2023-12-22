package com.malletsplay.eyecare.data.api

import com.malletsplay.eyecare.data.response.ResultResponse
import com.malletsplay.eyecare.data.response.UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("predicts")
    suspend fun getResult(): ResultResponse

    @Multipart
    @Headers(
        "Content-Type: application/json"
    )
    @POST("uploads")
    suspend fun uploadImage(
        @Part images: MultipartBody.Part
    ): UploadResponse
}