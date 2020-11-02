package com.example.protasks.presenters


interface ITaskPresenter {
    fun getTasks()
    fun filterTasks(name:String)
    fun getUsers(id:Long)
}
