package com.example.protasks

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance<T>(url_service: String, restClass: Class<T>) {
    val baseURL = "http://10.0.2.2:8080/"
    val service: T = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/$url_service")
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(restClass)
}