package com.example.protasks.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.protasks.AttachmentsAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.nio.file.Files
import java.util.*
import kotlin.collections.ArrayList


class Task(
    @Expose
    @SerializedName("title")
    private var title: String?, @Expose
    @SerializedName("description")
    private var description: String?, @Expose
    @SerializedName("taskList")
    private var tasklist: TaskList? = null
) : ITask,Comparable<Task> {
    override fun compareTo(other: Task): Int = this.position!!.compareTo(other.position!!)
    @Expose
    @SerializedName("id")
    private var id: Long? = null

    @Expose
    @SerializedName("position")
    private var position: Int? = null

    @Expose
    @SerializedName("tag_ids")
    private var tag_ids: List<Tag>? = null

    @Expose
    @SerializedName("date_end")
    private var date_end: Date? = null

    @Expose
    @SerializedName("users")
    private var users: List<User>? = null

    @Expose
    @SerializedName("attachments")
    private var attachments: List<File?>? = null

    @Expose
    @SerializedName("subTasks")
    private var subTasks: List<Task?>? = null

    @Expose
    @SerializedName("parent_task")
    private var parentTask: Task? = null

    private var selected:Boolean = false

    @Expose
    @SerializedName("priority")
    private var priority: Priority? = null

    @Expose
    @SerializedName("date_start_cycle_time")
    private var dateStartCycleTime: Date? = null

    @Expose
    @SerializedName("date_end_cycle_time")
    private var dateEndCycleTime: Date? = null

    @Expose
    @SerializedName("date_start_lead_time")
    private var dateStartLeadTime: Date? = null

    @Expose
    @SerializedName("date_end_lead_time")
    private var dateEndLeadTime: Date? = null

    @Expose
    @SerializedName("messages")
    private var messages: ArrayList<Message?>? = null

    fun getDateStartCycleTime(): Date? {
        return dateStartCycleTime
    }

    fun setDateStartCycleTime(dateStartCycleTime: Date) {
        this.dateStartCycleTime = dateStartCycleTime
    }

    fun getDateEndCycleTime(): Date? {
        return dateEndCycleTime
    }

    fun setDateEndCycleTime(dateEndCycleTime: Date) {
        this.dateEndCycleTime = dateEndCycleTime
    }
    fun getDateStartLeadTime(): Date? {
        return dateStartLeadTime
    }

    fun setDateStartLeadTime(dateStartLeadTime: Date) {
        this.dateStartLeadTime = dateStartLeadTime
    }

    fun getDateEndLeadTime(): Date? {
        return dateEndLeadTime
    }

    fun setDateEndLeadTime(dateEndLeadTime: Date) {
        this.dateEndLeadTime = dateEndLeadTime
    }

    fun setSelected(selected:Boolean){
        this.selected=selected
    }
    fun isSelected():Boolean{
        return selected
    }

    fun getSubtasks(): List<Task?>?{
        return subTasks
    }
    fun getParentTask():Task?{
        return parentTask
    }

    fun getMessages(): List<Message?>?{
        return messages
    }

    fun getTags(): List<Tag?>?{
        return tag_ids
    }
    fun setTags(tags:List<Tag>){
        this.tag_ids=tags
    }

    fun getUsers(): List<User>? {
        return users
    }

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

    override fun getPhotos(): ArrayList<Bitmap> {
        val files = this.getAttachments()
        val photos = ArrayList<Bitmap>()
        if (files != null) {
            for (file in files){
                var type=""
                if (file!!.getType()!=null) {
                    type = file.getType()!!.lowercase(Locale.ROOT)
                }
                if (AttachmentsAdapter.EXTENSIONS_IMAGES.contains(type)){
                    val imageBytes = Base64.decode(file.getContent(), Base64.DEFAULT)
                    val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    photos.add(decodedImage)
                }
            }
        }
        return photos
    }

    fun setId(id:Long){
        this.id=id
    }

    fun getDateEnd():Date?{
        return this.date_end
    }
    fun getAttachments(): List<File?>? {
        return this.attachments
    }
    fun setParentTask(parentTask:Task){
        this.parentTask=parentTask
    }

    fun getPriority(): Priority? {
        return this.priority
    }
    fun setPriority(priority: Priority){
        this.priority=priority
    }
    fun addMessage(m: Message?) {
        if (this.messages == null || this.messages!!.isEmpty()) {
            this.messages = ArrayList()
        }
        if (!this.messages!!.contains(m)) {
            this.messages!!.add(m)
        }
    }


}
