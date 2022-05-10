package com.example.protasks.views

import com.example.protasks.models.TaskList

interface IInsideBoardsView {
    fun setTaskLists(taskList: List<TaskList>)
    fun updateTasks(listsUpdated:List<TaskList>)
    fun showToast(message:String)

}