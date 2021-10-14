package com.example.protasks.presenters

import android.graphics.Bitmap
import com.example.protasks.models.User


interface IBoardPresenter:IPresenter {
    fun getBoards()
    fun getUser()
    fun getViewPref():Boolean
    fun getPhoto(u:User):Bitmap
    fun removePreferences()
    fun setViewPref(listMode:Boolean)
    fun filterBoards(name:String)
}
