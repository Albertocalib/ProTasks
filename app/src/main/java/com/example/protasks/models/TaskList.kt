package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TaskList {
    @Expose
    @SerializedName("id")
    private var id: Long = 0

    @Expose
    @SerializedName("title")
    private var title: String? = null

    @Expose
    @SerializedName("tasks")
    private var tasks: List<Task?>? = null

    @Expose
    @SerializedName("board")
    private var board: Board? = null

    @Expose
    @SerializedName("position")
    private var position: Int? = null

    fun getId(): Long {
        return id
    }

    fun setId(id: Long) {
        this.id = id
    }

    fun getTitle(): String? {
        return title
    }

    fun setTitle(title: String?) {
        this.title = title
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
    fun getPosition(): Int {
        return position!!
    }

    fun setPosition(position: Int) {
        this.position = position
    }

}