package com.example.protasks.restServices

import com.example.protasks.models.Tag
import retrofit2.Call
import retrofit2.http.*


interface TagRestService {

    @GET("tags/task_id={id}")
    fun getTagsByTask(@Path("id") id:Long):Call<List<Tag>>

    @DELETE("id={id}/task={task_id}")
    fun removeTagToTask(@Path("id") id:Long,@Path("task_id") taskId:Long):Call<Boolean>

    @POST("id={id}/tag={tag_id}")
    fun addTagToTask(@Path("id") id:Long,@Path("tag_id") userId:Long):Call<Boolean>

    @GET("board_id={id}")
    fun getTagsInBoard(@Path("id") id:Long):Call<List<Tag>>

    @POST("newTag/board={boardName}&username={username}")
    fun createTag(@Body tag: Tag?, @Path("boardName") boardName:String, @Path("username") username:String):Call<Tag>



}