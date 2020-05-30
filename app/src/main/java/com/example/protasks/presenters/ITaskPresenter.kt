package com.example.protasks.presenters

import android.graphics.Bitmap
import com.example.protasks.models.User


interface ITaskPresenter {
    fun getTasks()
    fun filterTasks(name:String)
}
