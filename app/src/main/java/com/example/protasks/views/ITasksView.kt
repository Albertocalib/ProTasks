package com.example.protasks.views

import com.example.protasks.models.Tag
import com.example.protasks.models.Task
import com.example.protasks.models.User

interface ITasksView {
    fun setTasks(tasks:List<Task>)
    fun setAssignments(users:List<User>)
    fun setUsers(users:List<User>)
    fun setTags(tags:List<Tag>)
    fun setTagsBoard(tags:List<Tag>)
    fun updateTags(tag:Tag)
    fun updateTask(t: Task)
}