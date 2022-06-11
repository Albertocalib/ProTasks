package com.example.protasks.presenters.task

import android.net.Uri
import com.example.protasks.models.*
import com.example.protasks.presenters.IPresenter
import java.util.*
import kotlin.collections.ArrayList


interface ITaskContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinishedSetTasks(successful: Boolean,code:Int,tasks: List<Task>)
            fun onFinishedSetAssignments(successful: Boolean,code:Int,users: List<User>)
            fun onFinishedSetUsers(successful: Boolean,code:Int,users: List<User>)
            fun onFinishedUpdateTask(successful: Boolean,code:Int,task: Task)
            fun onFinishedSetTags(successful: Boolean,code:Int,tags: List<Tag>)
            fun onFinishedSetTagsBoard(successful: Boolean,code:Int,tags: List<Tag>)
            fun onFailure(t: Throwable?)
            fun onFinishedUpdateTag(successful: Boolean,code:Int,tag: Tag,message: String)

        }

        fun getTasks(onFinishedListener: OnFinishedListener, username:String)
        fun filterTasks(onFinishedListener: OnFinishedListener,name:String, username: String)
        fun getUsers(onFinishedListener: OnFinishedListener,id:Long)
        fun getUsersInBoard(onFinishedListener: OnFinishedListener,boardId:Long)
        fun updateDate(onFinishedListener: OnFinishedListener,id:Long,date: Date)
        fun addAssignment(onFinishedListener: OnFinishedListener,taskId: Long, userId: Long)
        fun removeAssignment(onFinishedListener: OnFinishedListener,taskId: Long, userId: Long, update: Boolean)
        fun getTags(onFinishedListener: OnFinishedListener,id: Long)
        fun removeTag(onFinishedListener: OnFinishedListener,taskId: Long, tagId: Long,update:Boolean)
        fun addTag(onFinishedListener: OnFinishedListener,taskId: Long, tagId: Long)
        fun getTagsInBoard(onFinishedListener: OnFinishedListener,boardId: Long)
        fun createTag(onFinishedListener: OnFinishedListener, tag: Tag, boardName:String, username: String)
        fun updateTitle(onFinishedListener: OnFinishedListener,id: Long, title: String)
        fun updateDescription(onFinishedListener: OnFinishedListener,id: Long, description: String)
        fun addAttachedFile(onFinishedListener: OnFinishedListener,id: Long, files: HashSet<File>)
        fun removeAttachedFile(onFinishedListener: OnFinishedListener,file: File, task: Task)
        fun createSubTask(onFinishedListener: OnFinishedListener,taskP: Task, subTask: Task)
        fun deleteSubtasks(onFinishedListener: OnFinishedListener,stringSubtasks: String)
        fun updatePriority(onFinishedListener: OnFinishedListener,id: Long, priority: Priority)
    }

    interface View {
        fun setTasks(tasks:List<Task>)
        fun setAssignments(users:List<User>)
        fun setUsers(users:List<User>)
        fun setTags(tags:List<Tag>)
        fun setTagsBoard(tags:List<Tag>)
        fun updateTags(tag:Tag)
        fun updateTask(t: Task)
        fun setUser(u: User)
        fun addMessage(msg: Message)
        fun showToast(message:String)
        fun onResponseFailure(t: Throwable?)
    }

    interface Presenter:IPresenter {
        fun getTasks()
        fun filterTasks(name:String)
        fun getUsers(id:Long)
        fun getUsersInBoard(boardId:Long)
        fun updateDate(id:Long,date: Date)
        fun addAssignment(taskId: Long, userId: Long)
        fun removeAssignment(taskId: Long, userId: Long, update: Boolean)
        fun getTags(id: Long)
        fun addTag(taskId: Long, tagId: Long)
        fun getTagsInBoard(boardId: Long)
        fun createTag(boardName: String ,name:String,color:String)
        fun updateTitle(id: Long, title: String)
        fun updateDescription(id: Long, description: String)
        fun addAttachedFile(data: HashSet<Uri>, id: Long?)
        fun removeAttachedFile(file: File, task: Task)
        fun createSubTask(taskP: Task, subTaskTitle: String,subTaskDescription: String)
        fun deleteSubtasks(subtasksSelected: ArrayList<Task>)
        fun updatePriority(id: Long, priority: Priority)
    }
}
