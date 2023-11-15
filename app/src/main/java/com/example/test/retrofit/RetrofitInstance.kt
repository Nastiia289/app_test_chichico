package com.example.test.retrofit

import android.util.Log
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {
    private const val BASE_URL = "https://dev.cherest.com.ua/"

    val api: ApiService by lazy {
        val interceptor = HttpLoggingInterceptor { message -> Log.d("HTTP Request", message) }.apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
            this.connectTimeout(3, TimeUnit.SECONDS)
            this.readTimeout(20, TimeUnit.SECONDS)
            this.writeTimeout(25, TimeUnit.SECONDS)
        }.build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
