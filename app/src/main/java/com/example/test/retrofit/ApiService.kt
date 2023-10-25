package com.example.test.retrofit

import android.util.Log
import com.example.test.retrofit.RetrofitInstance.api
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("/api/v3/user/checkMobile/{phoneNumber}")
    suspend fun checkMobile(@Path("phoneNumber") phoneNumber: String): Call<UserData>

    @POST("/v3/user/registration/checkOtp")
    suspend fun sendOtp(@Body phoneNumber: UserData): Call<UserData>
}

suspend fun checkMobile(phoneNumber: String): Call<UserData> {
    Log.d("ApiService", "Sending checkMobile request for phoneNumber: $phoneNumber")
    return api.checkMobile(phoneNumber)
}


suspend fun sendOtp(phoneNumber: UserData): Call<UserData> {
    Log.d("ApiService", "Sending sendOtp request for phoneNumber: ${phoneNumber.phoneNumber}")
    return api.sendOtp(phoneNumber)
}