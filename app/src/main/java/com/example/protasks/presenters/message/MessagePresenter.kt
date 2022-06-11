package com.example.protasks.presenters.message

import com.example.protasks.models.*
import com.example.protasks.utils.PreferencesManager

class MessagePresenter(private var view: IMessageContract.View, private val preference: PreferencesManager): IMessageContract.IMessagePresenter,IMessageContract.Model.OnFinishedListener{
    private val messageModel: MessageModel = MessageModel()

    override fun getUser(){
        messageModel.getUser(this,preference.getEmail()!!)
    }


    override fun createMessage(message:Message){
        messageModel.createMessage(this,message)
    }

    override fun onFinishedGetUser(successful: Boolean, code: Int, user: User?) {
        view.setUser(user!!)
    }

    override fun onFinishedCreateMessage(successful: Boolean, code: Int, message: Message?) {
        view.addMessage(message!!)
    }

    override fun onFailure(t: Throwable?) {
        view.onResponseFailure(t)
    }


}