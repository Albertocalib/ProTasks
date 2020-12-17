package com.example.protasks.presenters


interface ITaskPresenter:IPresenter {
    fun getTasks()
    fun filterTasks(name:String)
    fun getUsers(id:Long)
    fun getUsersInBoard(boardId:Long)
}
