package com.example.protasks.restServices

import com.example.protasks.models.Task
import com.example.protasks.models.User
import retrofit2.Call
import retrofit2.http.*
import java.util.*


interface TaskRestService {

    @POST("newTask/board={boardName}&list={listName}&username={username}")
    fun createTask(@Body task: Task?, @Path("boardName") boardName:String,@Path("listName") listName:String, @Path("username") username:String):Call<Task>

    @GET("username={username}")
    fun getTasksByUser (@Path("username") username:String): Call<List<Task>>

    @GET("taskName={name}&username={username}")
    fun getTasksFilterByName (@Path("name") name:String,@Path("username") username:String): Call<List<Task>>

    @PUT("id={id}&newPosition={newPosition}&newTaskList={newTaskList}")
    fun updateTaskPosition(@Path("id") id:Long,@Path("newPosition") newPosition:Long,@Path("newTaskList")newTaskList:Long):Call<Task>

    @GET("users/task_id={id}")
    fun getUsersByTask(@Path("id") id:Long):Call<List<User>>

    @POST("id={id}/user={user_id}")
    fun addUserToTask(@Path("id") id:Long,@Path("user_id") userId:Long):Call<Boolean>

    @DELETE("id={id}/user={user_id}")
    fun removeUserToTask(@Path("id") id:Long,@Path("user_id") userId:Long):Call<Boolean>

    @PUT("id={id}&newDateEnd={newDate}")
    fun updateDateEnd(@Path("id") id:Long,@Path("newDate") newDate: Date):Call<Task>

    @PUT("id={id}&newTitle={title}")
    fun updateTitleTask(@Path("id") id: Long, @Path("title")title: String): Call<Task>

    @PUT("id={id}&newDescription={description}")
    fun updateDescriptionTask(@Path("id") id: Long, @Path("description")description: String): Call<Task>
}