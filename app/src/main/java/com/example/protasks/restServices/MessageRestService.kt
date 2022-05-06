package com.example.protasks.restServices

import com.example.protasks.models.Message
import retrofit2.Call
import retrofit2.http.*

interface MessageRestService {

    @POST("newMessage")
    fun createMessage(@Body message: Message?): Call<Message>?
}