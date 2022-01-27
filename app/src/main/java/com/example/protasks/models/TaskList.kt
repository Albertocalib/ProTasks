package com.example.protasks.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TaskList() :Parcelable {
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

    constructor(parcel: Parcel) : this() {
        id = parcel.readLong()
        title = parcel.readString()
        position = parcel.readValue(Int::class.java.classLoader) as? Int
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeValue(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskList> {
        override fun createFromParcel(parcel: Parcel): TaskList {
            return TaskList(parcel)
        }

        override fun newArray(size: Int): Array<TaskList?> {
            return arrayOfNulls(size)
        }
    }

}