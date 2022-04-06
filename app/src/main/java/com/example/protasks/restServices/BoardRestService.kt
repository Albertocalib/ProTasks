package com.example.protasks.restServices

import com.example.protasks.models.Board
import com.example.protasks.models.BoardUsersPermRel
import com.example.protasks.models.Rol
import retrofit2.Call
import retrofit2.http.*

interface BoardRestService {
    @GET("username={username}")
    fun getBoardsByUser(@Path("username") username: String): Call<List<Board>>

    @GET("boardName={name}&username={username}")
    fun getBoardsFilterByName(
        @Path("name") name: String,
        @Path("username") username: String
    ): Call<List<Board>>

    @POST("newBoard/username={username}")
    fun createBoard(@Body board: Board?, @Path("username") username: String): Call<Board>

    @PUT("id={id}/wipActivated={wipActivated}&wipLimit={wipLimit}&wipList={wipList}")
    fun updateWIP(
        @Path("id") id: Long,
        @Path("wipActivated") wipActivated: Boolean,
        @Path("wipLimit") wipLimit: Int,
        @Path("wipList") wipList: String
    ): Call<Board>

    @PUT("id={id}/username={username}&rol{rol}")
    fun addUserToBoard(
        @Path("id") id: Long,
        @Path("username") username: String,
        @Path("rol") rol: Rol
    ): Call<Board>

    @PUT("id={id}/userId={userId}&role={role}")
    fun updateRole(
        @Path("id") id: Long,
        @Path("userId") userId: Long,
        @Path("role") role: Rol
    ): Call<Board>

    @DELETE("id={id}/userId={user_id}")
    fun deleteUserFromBoard(@Path("id") id: Long, @Path("user_id") userId: Long): Call<Board>

    @GET("id={id}/userId={user_id}")
    fun getRol(@Path("id") id: Long, @Path("user_id") userId: Long): Call<BoardUsersPermRel>

    @PUT("id={id}/timeActivated={timeActivated}&cycleStart={cycleStart}&cycleEnd={cycleEnd}&leadStart={leadStart}&leadEnd={leadEnd}")
    fun updateTime(
        @Path("id") id: Long,
        @Path("timeActivated") wipActivated: Boolean,
        @Path("cycleStart") cycleStart: String,
        @Path("cycleEnd") cycleEnd: String,
        @Path("leadStart") leadStart: String,
        @Path("leadEnd") leadEnd: String
    ): Call<Board>

}