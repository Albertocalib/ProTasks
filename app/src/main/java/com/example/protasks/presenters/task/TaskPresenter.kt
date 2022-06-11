package com.example.protasks.presenters.task

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Base64
import android.webkit.MimeTypeMap
import com.example.protasks.models.*
import com.example.protasks.utils.PreferencesManager
import java.util.*
import kotlin.collections.HashSet


class TaskPresenter(
    private var view: ITaskContract.View,
    private val preference: PreferencesManager,
    private val contentResolver: ContentResolver
) :
    ITaskContract.Presenter, ITaskContract.Model.OnFinishedListener {
    private val taskModel: TaskModel = TaskModel()

    override fun getTasks() {
        taskModel.getTasks(this, preference.getEmail()!!)
    }

    override fun filterTasks(name: String) {
        val username = preference.getEmail()
        taskModel.filterTasks(this, name, username!!)
    }

    override fun getUsers(id: Long) {
        taskModel.getUsers(this, id)
    }

    override fun getUsersInBoard(boardId: Long) {
        taskModel.getUsersInBoard(this, boardId)
    }

    override fun updateDate(id: Long, date: Date) {
        taskModel.updateDate(this, id, date)
    }

    override fun addAssignment(taskId: Long, userId: Long) {
        taskModel.addAssignment(this, taskId, userId)
    }

    override fun removeAssignment(taskId: Long, userId: Long, update: Boolean) {
        taskModel.removeAssignment(this, taskId, userId, update)
    }

    override fun getTags(id: Long) {
        taskModel.getTags(this, id)
    }

    override fun removeTag(taskId: Long, tagId: Long, update: Boolean) {
        taskModel.removeTag(this, taskId, tagId, update)
    }

    override fun addTag(taskId: Long, tagId: Long) {
        taskModel.addTag(this, taskId, tagId)
    }

    override fun getTagsInBoard(boardId: Long) {
        taskModel.getTagsInBoard(this, boardId)
    }

    override fun createTag(boardName: String, name: String, color: String) {
        val username = preference.getEmail()
        val t = Tag()
        t.setName(name)
        t.setColor(color)
        taskModel.createTag(this, t, boardName, username!!)
    }

    override fun updateTitle(id: Long, title: String) {
        taskModel.updateTitle(this, id, title)
    }

    override fun updateDescription(id: Long, description: String) {
        taskModel.updateDescription(this, id, description)
    }

    private fun fileToBase64(uri: Uri): String {
        val imageStream = contentResolver.openInputStream(uri)!!.readBytes()
        return Base64.encodeToString(imageStream, Base64.NO_WRAP)
    }

    private fun getFileName(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor!!.count > 0) {
            cursor.moveToFirst()
            val fileName: String =
                cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            cursor.close()
            return fileName
        }
        return ""
    }

    override fun addAttachedFile(data: HashSet<Uri>, id: Long?) {
        val files = HashSet<File>()
        for (file in data) {
            val mime = MimeTypeMap.getSingleton()
            val type = mime.getExtensionFromMimeType(contentResolver.getType(file))
            val fileName = getFileName(file)
            val fileBase64 = fileToBase64(file)
            files.add(File(fileName, type, fileBase64))
        }
        taskModel.addAttachedFile(this, id!!, files)
    }

    override fun removeAttachedFile(file: File, task: Task) {
        taskModel.removeAttachedFile(this, file, task)
    }

    override fun createSubTask(taskP: Task, subTaskTitle: String, subTaskDescription: String) {
        val t = Task(subTaskTitle, subTaskDescription)
        taskModel.createSubTask(this, taskP, t)
    }

    override fun deleteSubtasks(subtasksSelected: ArrayList<Task>) {
        var stringSubtasks = ""
        for (subtask in subtasksSelected) {
            stringSubtasks += subtask.getId().toString() + "&"
        }
        taskModel.deleteSubtasks(this, stringSubtasks)
    }

    override fun updatePriority(id: Long, priority: Priority) {
        taskModel.updatePriority(this, id, priority)
    }

    override fun onFinishedSetTasks(successful: Boolean, code: Int, tasks: List<Task>) {
        if (successful){
            view.setTasks(tasks)
        }
    }

    override fun onFinishedSetAssignments(successful: Boolean, code: Int, users: List<User>) {
        if (successful){
            view.setAssignments(users)
        }
    }

    override fun onFinishedSetUsers(successful: Boolean, code: Int, users: List<User>) {
        if (successful){
            view.setUsers(users)
        }
    }

    override fun onFinishedUpdateTask(successful: Boolean, code: Int, task: Task) {
        if (successful){
            view.updateTask(task)
        }
    }

    override fun onFinishedSetTags(successful: Boolean, code: Int, tags: List<Tag>) {
        if (successful){
            view.setTags(tags)
        }
    }

    override fun onFinishedSetTagsBoard(successful: Boolean, code: Int, tags: List<Tag>) {
        if (successful){
            view.setTagsBoard(tags)
        }
    }

    override fun onFailure(t: Throwable?) {
        view.onResponseFailure(t)
    }

    override fun onFinishedUpdateTag(successful: Boolean, code: Int, tag: Tag, message: String) {
        if (successful){
            view.updateTags(tag)
            view.showToast(message)
        }
    }
}