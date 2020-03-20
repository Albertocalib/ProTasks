package com.example.protasks.restServices



import com.example.protasks.models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.POST


interface UserRestService {
    @POST("logIn")
    @FormUrlEncoded
    fun logInAttempt(@Field("username") username:String,@Field("password")password:String): Call<User>

    @POST("register/newUser")
    fun createUser(@Body user: User?): Call<User>?
}