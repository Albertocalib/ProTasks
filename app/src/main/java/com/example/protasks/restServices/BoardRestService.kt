package com.example.protasks.restServices



import com.example.protasks.models.Board
import retrofit2.Call
import retrofit2.http.*


interface BoardRestService {
    @GET("{username}")
    fun getBoardsByUser (@Path("username") username:String): Call<List<Board>>

}