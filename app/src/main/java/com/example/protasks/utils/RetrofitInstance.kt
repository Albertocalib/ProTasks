package com.example.protasks.utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance<T>(urlService: String, restClass: Class<T>) {
    val service: T = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/$urlService")
        .addConverterFactory(GsonConverterFactory.create()).client( OkHttpClient.Builder().build())
        .build().create(restClass)
    val service2: T = Retrofit.Builder().baseUrl("http://192.168.0.24:8080/$urlService")
        .addConverterFactory(GsonConverterFactory.create()).client( OkHttpClient.Builder().build())
        .build().create(restClass)
}