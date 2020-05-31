package com.example.protasks.restServices

import com.example.protasks.models.TaskList
import retrofit2.Call
import retrofit2.http.*

interface TaskListRestService {

    @POST("newList/board={boardName}&username={username}")
    fun createList(@Body taskList: TaskList?,@Path("boardName") boardName:String,@Path("username") username:String):Call<TaskList>


}