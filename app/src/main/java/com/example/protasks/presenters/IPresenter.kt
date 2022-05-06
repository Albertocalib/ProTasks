package com.example.protasks.presenters


interface IPresenter{
    fun removeTag(taskId: Long, tagId: Long,update:Boolean)
}
