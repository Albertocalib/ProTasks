package com.example.protasks.presenters

import android.content.Context
import android.util.Log
import com.example.protasks.utils.RetrofitInstance
import com.example.protasks.models.*
import com.example.protasks.restServices.*
import com.example.protasks.utils.Preference
import com.example.protasks.views.ITasksView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessagePresenter(private var view: ITasksView, private val preference: Preference){
    private val retrofitInsUser: RetrofitInstance<UserRestService> =
        RetrofitInstance("api/user/", UserRestService::class.java)

    private val retrofitInsMessage: RetrofitInstance<MessageRestService> =
        RetrofitInstance("/api/message/", MessageRestService::class.java)

    fun getUser(){
        val username = preference.getEmail()
        val user = retrofitInsUser.service.getUser(username!!)
        user.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                view.setUser(response!!.body()!!)

            }

        })
    }

    fun createMessage(message:Message){
        val msg = retrofitInsMessage.service.createMessage(message)
        msg!!.enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                view.addMessage(response!!.body()!!)

            }

        })
    }



}