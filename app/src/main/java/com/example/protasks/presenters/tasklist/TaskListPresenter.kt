package com.example.protasks.presenters.tasklist

import android.content.Context
import android.widget.ImageView
import com.example.protasks.models.*
import com.example.protasks.utils.*


class TaskListPresenter(private var view: ITaskListContract.View?, private val preference: PreferencesManager) :
    ITaskListContract.ITaskListPresenter, ITaskListContract.Model.OnFinishedListener {

    private val taskListModel:TaskListModel=TaskListModel()
    private val imageHandler: ImageHandler = ImageHandler()


    override fun downloadImage(image: ImageView, context: Context) {
        imageHandler.downloadImage(image, context)
    }

    override fun createTaskList(boardName: String, listName: String) {
        val username = preference.getEmail()
        taskListModel.createTaskList(this,boardName,listName,username!!)

    }

    override fun getLists(boardName: String) {
        val username = preference.getEmail()
        taskListModel.getLists(this,boardName,username!!,"")
    }
    override fun updateTaskListPosition(id:Long,position: Long) {
        taskListModel.updateTaskListPosition(this,id,position)
    }
    override fun updateTaskPosition(id:Long,newPosition:Long,newTaskList:Long,listMode:Boolean){
        taskListModel.updateTaskPosition(this,id,newPosition,newTaskList,listMode)
    }
    override fun updateLists(id:Long){
        taskListModel.updateLists(this,id)
    }
    override fun createTask(boardName: String,taskName:String,listName: String,description:String){
        val username = preference.getEmail()
        val t = Task(taskName,description)
        taskListModel.createTask(this,t,boardName,listName,description,username!!)

    }
    override fun deleteTaskList(boardName: String,listName: String){
        val username = preference.getEmail()
        taskListModel.deleteTaskList(this,boardName,listName,username!!)
    }
    override fun getBoards(view: ITaskListContract.ViewBottomSheet) {
        val username = preference.getEmail()
        taskListModel.getBoards(this,view,username!!)
    }

    override fun moveTaskList(board: Board, listName: String, boardDestId: Long) {
        val username = preference.getEmail()
        taskListModel.moveTaskList(this,board,listName,boardDestId,username!!)
    }

    override fun copyTaskList(board: Board, listName: String, boardDestId: Long) {
        val username = preference.getEmail()
        taskListModel.copyTaskList(this, board,listName,boardDestId,username!!)
    }
    override fun deleteTask(taskId:Long,boardName: String){
        val username = preference.getEmail()
        taskListModel.deleteTask(this, taskId,boardName,username!!)
    }

    override fun copyTask(board: Board, listName: String, taskId: Long?, boardDestId: Long) {
        val username = preference.getEmail()
        taskListModel.copyTask(this,board,listName,taskId,boardDestId,username!!)
    }
    override fun moveTask(board: Board, listName: String, taskId: Long?, boardDestId: Long) {
        val username = preference.getEmail()
        taskListModel.moveTask(this,board,listName,taskId,boardDestId,username!!)
    }

    override fun removeTag(taskId: Long, tagId: Long,update:Boolean) {
        taskListModel.removeTag(this,taskId,tagId,update)
    }

    override fun onFinishedSetLists(successful: Boolean, code: Int, lists: List<TaskList>?, message:String) {
        if (successful){
            if (message!="")
                view!!.showToast(message)
            (view!! as ITaskListContract.ViewNormal).setTaskLists(lists!!)
        }
    }

    override fun onFinishedUpdateTasks(successful: Boolean, code: Int, lists: List<TaskList>?) {
        if (successful) {
            (view!! as ITaskListContract.ViewNormal).updateTasks(lists!!)
        }
    }

    override fun onFinishedSetBoard(successful: Boolean, code: Int, board: List<Board>?) {
        if (successful) {
            (view!! as ITaskListContract.ViewBottomSheet).setBoard(board!!)
        }
    }

    override fun onFailure(t: Throwable?) {
        view!!.onResponseFailure(t)
    }

}