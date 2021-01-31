package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class File(
    @Expose
    @SerializedName("name")
    private var name: String? = null,

    @Expose
    @SerializedName("type")
    private var type: String? = null,

    @Expose
    @SerializedName("content")
    private var content: String? = null
) {
    @Expose
    @SerializedName("id")
    private var id: Long = 0

    @Expose
    @SerializedName("task")
    private var task: Task? = null


    fun getType(): String? {
        return type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun setId(id: Long) {
        this.id = id
    }


    fun getId(): Long {
        return id
    }


    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getTask(): Task? {
        return task
    }

    fun setTask(task: Task) {
        this.task = task
    }


}