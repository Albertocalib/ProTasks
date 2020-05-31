package com.example.protasks

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance<T>(urlService: String, restClass: Class<T>) {
    val service: T = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/$urlService")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(restClass)
}