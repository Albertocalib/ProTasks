package com.example.protasks.restServices

import com.example.protasks.models.Board
import retrofit2.Call
import retrofit2.http.*

interface BoardRestService {
    @GET("username={username}")
    fun getBoardsByUser (@Path("username") username:String): Call<List<Board>>

    @GET("boardName={name}&username={username}")
    fun getBoardsFilterByName (@Path("name") name:String,@Path("username") username:String): Call<List<Board>>

    @POST("newBoard/username={username}")
    fun createBoard(@Body board: Board?,@Path("username") username:String):Call<Board>

    @PUT("id={id}/wipActivated={wipActivated}&wipLimit={wipLimit}&wipList={wipList}")
    fun updateWIP( @Path("id") id: Long,
                   @Path("wipActivated") wipActivated: Boolean,
                   @Path("wipLimit") wipLimit: Int,
                   @Path("wipList") wipList: String):Call<Board>


}