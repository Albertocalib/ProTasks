package com.example.protasks.presenters


interface ITaskListPresenter:IPresenter{
    fun getLists(boardName:String)
    fun updateTaskListPosition(id:Long,position:Long)
    fun updateTaskPosition(id:Long,newPosition:Long,newTaskList:Long,listMode:Boolean)
    fun deleteTaskList(boardName: String, listName: String)
}
