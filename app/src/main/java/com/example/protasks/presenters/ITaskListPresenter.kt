package com.example.protasks.presenters


interface ITaskListPresenter {
    fun getLists(boardName:String)
    fun updateTaskListPosition(id:Long,position:Long)
    fun updateTaskPosition(id:Long,newPosition:Long,newTaskList:Long)
}
