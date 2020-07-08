package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Task(
    @Expose
    @SerializedName("title")
    private var title: String?, @Expose
    @SerializedName("description")
    private var description: String?, @Expose
    @SerializedName("taskList")
    private var tasklist: TaskList? = null
) : ITask {

    @Expose
    @SerializedName("id")
    private var id: Long? = null

    @Expose
    @SerializedName("position")
    private var position: Int? = null


    override fun getTitle(): String? {
        return title
    }

    override fun getDescription(): String? {
        return description
    }

    override fun getPosition(): Int? {
        return position
    }

    override fun getId(): Long? {
        return id
    }

    override fun setTitle(title: String) {
        this.title = title
    }

    override fun setDescription(description: String) {
        this.description = description
    }

    override fun setPosition(position: Int) {
        this.position = position
    }
    override fun getTaskList():TaskList{
        return tasklist!!
    }
    override fun setTaskList(tasklist:TaskList){
        this.tasklist=tasklist
    }
    fun setId(id:Long){
        this.id=id
    }


}
