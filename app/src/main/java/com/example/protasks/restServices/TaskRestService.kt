package com.example.protasks.restServices

import com.example.protasks.models.Task
import com.example.protasks.models.TaskList
import retrofit2.Call
import retrofit2.http.*


interface TaskRestService {

    @POST("newTask/board={boardName}&list={listName}&username={username}")
    fun createTask(@Body task: Task?, @Path("boardName") boardName:String,@Path("listName") listName:String, @Path("username") username:String):Call<Task>


}