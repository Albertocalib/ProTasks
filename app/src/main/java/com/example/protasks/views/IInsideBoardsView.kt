package com.example.protasks.views

import com.example.protasks.models.Board
import com.example.protasks.models.TaskList
import com.example.protasks.models.User

interface IInsideBoardsView {
    fun setTaskLists(taskList: List<TaskList>)
    fun updateTasks(listsUpdated:List<TaskList>)
}