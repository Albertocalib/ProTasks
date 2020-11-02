package com.example.protasks.views

import com.example.protasks.models.Task
import com.example.protasks.models.User

interface ITasksView {
    fun setTasks(tasks:List<Task>)
    fun setAssignments(users:List<User>)
}