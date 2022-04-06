package com.example.protasks.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class Board {
    @Expose
    @SerializedName("id")
    private var id: Long = 0

    @Expose
    @SerializedName("name")
    private var name: String? = null

    @Expose
    @SerializedName("photo")
    private var photo: String? = null

    @Expose
    @SerializedName("taskLists")
    private var taskLists: List<TaskList?>? = ArrayList()

    @Expose
    @SerializedName("users")
    private val users: ArrayList<BoardUsersPermRel>? = ArrayList()

    @Expose
    @SerializedName("tags")
    private var tags: List<Tag>? = null

    @Expose
    @SerializedName("wipActivated")
    private var wipActivated: Boolean = false

    @Expose
    @SerializedName("wipLimit")
    private var wipLimit: Int = 0

    @Expose
    @SerializedName("wipList")
    private var wipList: String? = null

    @Expose
    @SerializedName("timeActivated")
    private var timeActivated: Boolean = false

    @Expose
    @SerializedName("cycleStartList")
    private var cycleStartList: String? = null

    @Expose
    @SerializedName("cycleEndList")
    private var cycleEndList: String? = null

    @Expose
    @SerializedName("leadStartList")
    private var leadStartList: String? = null

    @Expose
    @SerializedName("leadEndList")
    private var leadEndList: String? = null

    @Expose
    @SerializedName("create_date")
    private val createDate: Date? = null

    fun getCreatedDate(): Date? {
        return createDate
    }

    fun getLeadStartList(): String? {
      return leadStartList
    }
    fun getLeadEndList(): String? {
        return leadEndList
    }
    fun setLeadStartList(leadStartList: String) {
        this.leadStartList = leadStartList
    }
    fun setLeadEndList(leadEndList: String) {
        this.leadEndList = leadEndList
    }
    fun getCycleStartList(): String? {
        return cycleStartList
    }
    fun getCycleEndList(): String? {
        return cycleEndList
    }
    fun setCycleStartList(cycleStartList: String) {
        this.cycleStartList = cycleStartList
    }
    fun setCycleEndList(cycleEndList: String) {
        this.cycleEndList = cycleEndList
    }
    fun getTimeActivated(): Boolean {
        return timeActivated
    }

    fun setTimeActivated(timeActivated: Boolean) {
        this.timeActivated = timeActivated
    }
    fun getWipList(): String? {
        return wipList
    }

    fun setWipList(wipList: String) {
        this.wipList = wipList
    }


    fun getWipActivated(): Boolean {
        return wipActivated
    }

    fun setWipActivated(wipActivated: Boolean) {
        this.wipActivated = wipActivated
    }

    fun getWipLimit(): Int {
        return wipLimit
    }

    fun setWipLimit(wipLimit: Int) {
        this.wipLimit = wipLimit
    }

    fun getPhoto(): String? {
        return photo
    }

    fun setPhoto(photo: String) {
        this.photo = photo
    }

    fun setId(photo: String) {
        this.photo = photo
    }


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

    fun getTaskLists(): List<TaskList?>? {
        return taskLists
    }

    fun setTaskLists(taskLists: List<TaskList?>?) {
        this.taskLists = taskLists
    }

    fun addUserOwner(u: User) {
        val b = BoardUsersPermRel()
        b.setRol(Rol.OWNER)
        b.setUser(u)
        b.setBoard(this)
        this.users!!.add(b)
    }
    fun getUsers():ArrayList<BoardUsersPermRel> {
        return this.users!!
    }
}