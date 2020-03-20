package com.example.protasks.presenters.login


interface ILoginPresenter {
    fun doLogin(userName:String, password:String)
    fun setProgressBarVisiblity(visiblity:Int)
}
