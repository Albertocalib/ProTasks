package com.example.protasks.restServices

import com.example.protasks.models.TaskList
import retrofit2.Call
import retrofit2.http.*

interface TaskListRestService {

    @POST("newList/board={boardName}&username={username}")
    fun createList(@Body taskList: TaskList?,@Path("boardName") boardName:String,@Path("username") username:String):Call<TaskList>

    @GET("board={boardName}&username={username}")
    fun getTaskListsByBoard(@Path("boardName") boardName:String,@Path("username") username:String):Call<List<TaskList>>

    @PUT("id={id}&position={position}")
    fun updateTaskListPosition(@Path("id") id:Long,@Path("position") position:Long):Call<TaskList>


}