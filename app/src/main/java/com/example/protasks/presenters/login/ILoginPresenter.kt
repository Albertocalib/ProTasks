package com.example.protasks.presenters.login

interface ILoginPresenter {
    fun doLogin(userName:String, password:String, keepLogin:Boolean)
    fun setProgressBarVisiblity(visiblity:Int)
    fun cheekKeepLogin()
}
