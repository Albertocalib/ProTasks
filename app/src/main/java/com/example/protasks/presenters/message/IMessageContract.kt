package com.example.protasks.presenters.message

import com.example.protasks.models.*

interface IMessageContract {

    interface Model {
        interface OnFinishedListener {
            fun onFinishedGetUser(successful: Boolean, code: Int, user: User?)
            fun onFinishedCreateMessage(successful: Boolean, code: Int, message: Message?)
            fun onFailure(t: Throwable?)
        }

        fun createMessage(onFinishedListener: OnFinishedListener?, message: Message)
        fun getUser(onFinishedListener: OnFinishedListener?, username: String)
    }

    interface View {
        fun onResponseFailure(t: Throwable?)
        fun setUser(u:User)
        fun addMessage(msg:Message)
    }

    interface IMessagePresenter {
        fun createMessage(message: Message)
        fun getUser()

    }
}
