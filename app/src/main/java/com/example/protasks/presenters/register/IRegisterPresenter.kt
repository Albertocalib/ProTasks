package com.example.protasks.presenters.register

interface IRegisterPresenter {
    fun createUser(name:String,surname:String,userName: String, password: String,email:String)
    fun setProgressBarVisiblity(visiblity:Int)
    fun checkEmail(email:String):Boolean
}