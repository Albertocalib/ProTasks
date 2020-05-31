package com.example.protasks.presenters.login

interface ILoginPresenter {
    fun doLogin(userName:String, password:String, keep_login:Boolean)
    fun setProgressBarVisiblity(visiblity:Int)
    fun cheekKeepLogin()
}
