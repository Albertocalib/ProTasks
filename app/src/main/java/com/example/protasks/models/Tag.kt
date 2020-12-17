package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Tag {
    @Expose
    @SerializedName("id")
    private var id: Long = 0

    @Expose
    @SerializedName("name")
    private var name: String? = null

    @Expose
    @SerializedName("color")
    private var color: String? = null

    @Expose
    @SerializedName("tasks")
    private var tasks: List<Task?>? = null

    @Expose
    @SerializedName("board")
    private var board: Board? = null


    fun getId(): Long {
        return id
    }

    fun setId(id: Long) {
        this.id = id
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getTasks(): List<Task?>? {
        return tasks
    }

    fun setTasks(tasks: List<Task?>?) {
        this.tasks = tasks
    }

    fun getBoard(): Board? {
        return board
    }

    fun setBoard(board: Board?) {
        this.board = board
    }
    fun getColor(): String? {
        return color
    }

    fun setColor(color: String?) {
        this.color = color
    }

}

