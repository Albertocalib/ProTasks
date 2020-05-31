package com.example.protasks.views

import com.example.protasks.models.Task

interface ITasksView {
    fun setTasks(tasks:List<Task>)
}