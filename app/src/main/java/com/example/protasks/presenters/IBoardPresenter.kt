package com.example.protasks.presenters

import android.graphics.Bitmap
import com.example.protasks.models.User


interface IBoardPresenter {
    fun getBoards()
    fun getUser()
    fun getViewPref():Boolean
    fun getPhoto(user:User):Bitmap
}
