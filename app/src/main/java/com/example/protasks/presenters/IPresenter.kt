package com.example.protasks.presenters

import com.example.protasks.models.Task


interface IPresenter{
    fun removeTag(taskId: Long, tagId: Long,update:Boolean)
}
