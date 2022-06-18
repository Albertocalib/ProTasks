package com.example.protasks.presenters.login

import com.example.protasks.models.User


interface ILoginContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinished(successful: Boolean,code:Int,user: User?)
            fun onFailure(t: Throwable?)
        }

        fun doLogin(onFinishedListener: OnFinishedListener?, userName: String, password: String)
    }

    interface View {
        fun onLoginResult(result: Boolean?, code: Int)
        fun onSetProgressBarVisibility(visibility: Int)
        fun goToSignUpActivity()
        fun goToMainactivity()
        fun sendLoginResult(successful: Boolean, code: Int)
        fun onResponseFailure(t:Throwable?)
        fun getUserName():String
        fun getPassword():String
    }

    interface Presenter:Model.OnFinishedListener {
        fun doLogin(userName:String, password:String, keepLogin:Boolean)
        fun setProgressBarVisiblity(visiblity:Int)
        fun cheekKeepLogin()
    }
}
