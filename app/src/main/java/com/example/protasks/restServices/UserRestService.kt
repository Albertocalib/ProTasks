package com.example.protasks.restServices

import com.example.protasks.models.User
import retrofit2.Call
import retrofit2.http.*

interface UserRestService {
    @POST("logIn")
    @FormUrlEncoded
    fun logInAttempt(@Field("username") username:String,@Field("password")password:String): Call<User>

    @POST("register/newUser")
    fun createUser(@Body user: User?): Call<User>?

    @GET("{username}")
    fun getUser (@Path("username") username:String): Call<User>

    @PUT("updatePhoto/{username}")
    fun updatePhoto(@Body photo: String?,@Path("username")username: String?): Call<User>?
}