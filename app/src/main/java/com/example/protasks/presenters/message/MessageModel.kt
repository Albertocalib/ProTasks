package com.example.protasks.presenters.message

import com.example.protasks.models.*
import com.example.protasks.restServices.*
import com.example.protasks.utils.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessageModel : IMessageContract.Model {
    private val retrofitInsUser: RetrofitInstance<UserRestService> =
        RetrofitInstance("api/user/", UserRestService::class.java)

    private val retrofitInsMessage: RetrofitInstance<MessageRestService> =
        RetrofitInstance("/api/message/", MessageRestService::class.java)

    override fun getUser(
        onFinishedListener: IMessageContract.Model.OnFinishedListener?,
        username: String
    ) {
        val user = retrofitInsUser.service.getUser(username)
        user.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                onFinishedListener!!.onFinishedGetUser(response!!.isSuccessful,response.code(),response.body()!!)

            }

        })
    }


    override fun createMessage(
        onFinishedListener: IMessageContract.Model.OnFinishedListener?,
        message: Message
    ) {
        val msg = retrofitInsMessage.service.createMessage(message)
        msg!!.enqueue(object : Callback<Message> {
            override fun onFailure(call: Call<Message>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<Message>?, response: Response<Message>?) {
                onFinishedListener!!.onFinishedCreateMessage(response!!.isSuccessful,response.code(),response.body()!!)

            }

        })
    }

}