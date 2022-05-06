package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Message {
    @Expose
    @SerializedName("id")
    private var id: Long = 0

    @Expose
    @SerializedName("task")
    private var task: Task? = null

    @Expose
    @SerializedName("body")
    private var body: String? = null

    @Expose
    @SerializedName("user")
    private var user: User? = null

    fun setId(id: Long) {
        this.id = id
    }


    fun getId(): Long {
        return id
    }


    fun getBody(): String? {
        return body
    }

    fun setBody(body: String?) {
        this.body = body
    }

    fun getTask(): Task? {
        return task
    }

    fun setTask(task: Task) {
        this.task = task
    }

    fun getUser(): User? {
        return user
    }

    fun setUser(user: User) {
        this.user = user
    }


}