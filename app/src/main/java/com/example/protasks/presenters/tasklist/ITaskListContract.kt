package com.example.protasks.presenters.tasklist

import android.content.Context
import android.widget.ImageView
import com.example.protasks.models.*
import com.example.protasks.presenters.IPresenter

interface ITaskListContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinishedSetLists(successful: Boolean, code: Int, lists: List<TaskList>?, message: String)
            fun onFinishedUpdateTasks(successful: Boolean, code: Int, lists: List<TaskList>?)
            fun onFinishedSetBoard(successful: Boolean, code: Int, board: List<Board>?)
            fun onFailure(t: Throwable?)
        }

        fun getLists(onFinishedListener: OnFinishedListener, boardName:String, username: String, message: String)
        fun updateTaskListPosition(onFinishedListener: OnFinishedListener, id:Long,position:Long)
        fun updateTaskPosition(onFinishedListener: OnFinishedListener, id:Long,newPosition:Long,newTaskList:Long,listMode:Boolean)
        fun deleteTaskList(onFinishedListener: OnFinishedListener, boardName: String, listName: String, username: String)
        fun createTaskList(onFinishedListener:OnFinishedListener,boardName: String, listName: String, username:String)
        fun updateLists(onFinishedListener: OnFinishedListener, id:Long)
        fun createTask(onFinishedListener: OnFinishedListener,task:Task, boardName: String,listName: String,description:String,username: String)
        fun copyTask(onFinishedListener: OnFinishedListener, board: Board, listName: String, taskId: Long?, boardDestId: Long, username: String)
        fun moveTask(onFinishedListener: OnFinishedListener, board: Board, listName: String, taskId: Long?, boardDestId: Long, username: String)
        fun deleteTask(onFinishedListener: OnFinishedListener, taskId:Long,boardName: String, username: String)
        fun copyTaskList(onFinishedListener: OnFinishedListener, board: Board, listName: String, boardDestId: Long, username: String)
        fun moveTaskList(onFinishedListener: OnFinishedListener, board: Board, listName: String, boardDestId: Long, username: String)
        fun getBoards(onFinishedListener: OnFinishedListener,view: ViewBottomSheet, username: String)
        fun removeTag(onFinishedListener: OnFinishedListener, taskId: Long, tagId: Long,update:Boolean)

    }
    interface View {
        fun onResponseFailure(t: Throwable?)
        fun showToast(message:String)
    }

    interface ViewNormal:View {
        fun setTaskLists(taskList: List<TaskList>)
        fun updateTasks(listsUpdated:List<TaskList>)
    }
    interface ViewBottomSheet:View {
        fun setBoard(boards: List<Board>)
    }

    interface ITaskListPresenter: IPresenter {
        fun getLists(boardName:String)
        fun updateTaskListPosition(id:Long,position:Long)
        fun updateTaskPosition(id:Long,newPosition:Long,newTaskList:Long,listMode:Boolean)
        fun deleteTaskList(boardName: String, listName: String)
        fun createTaskList(boardName: String, listName: String)
        fun downloadImage(image: ImageView, context: Context)
        fun updateLists(id:Long)
        fun createTask(boardName: String,taskName:String,listName: String,description:String)
        fun copyTask(board: Board, listName: String, taskId: Long?, boardDestId: Long)
        fun moveTask(board: Board, listName: String, taskId: Long?, boardDestId: Long)
        fun deleteTask(taskId:Long,boardName: String)
        fun copyTaskList(board: Board, listName: String, boardDestId: Long)
        fun moveTaskList(board: Board, listName: String, boardDestId: Long)
        fun getBoards(view: ViewBottomSheet)
    }
}
