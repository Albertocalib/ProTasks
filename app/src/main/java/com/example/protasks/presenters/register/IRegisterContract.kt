package com.example.protasks.presenters.register

import com.example.protasks.models.User


interface IRegisterContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinished(successful: Boolean,code:Int)
            fun onFailure(t: Throwable?)
        }

        fun createUser(onFinishedListener: OnFinishedListener?, user: User)
    }

    interface View {
        fun onRegisterResult(result: Boolean?, code: Int)
        fun onSetProgressBarVisibility(visibility: Int)
        fun postResult(successful: Boolean?, code: Int)
        fun onResponseFailure(t: Throwable?)
    }

    interface Presenter {
        fun createUser(name:String,surname:String,userName: String, password: String,email:String)
        fun setProgressBarVisiblity(visiblity:Int)
        fun checkEmail(email:String):Boolean
    }
}
