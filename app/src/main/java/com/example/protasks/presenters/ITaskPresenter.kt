package com.example.protasks.presenters

import java.util.*


interface ITaskPresenter:IPresenter {
    fun getTasks()
    fun filterTasks(name:String)
    fun getUsers(id:Long)
    fun getUsersInBoard(boardId:Long)
    fun updateDate(id:Long,date:Date)
}
