package com.example.protasks.presenters.register

import com.example.protasks.utils.RetrofitInstance
import com.example.protasks.restServices.UserRestService
import com.example.protasks.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.protasks.presenters.register.IRegisterContract.Model.OnFinishedListener


class RegisterModel :
    IRegisterContract.Model {
    private val retrofitIns: RetrofitInstance<UserRestService> = RetrofitInstance("api/user/",UserRestService::class.java)

    override fun createUser(onFinishedListener: OnFinishedListener?, user:User) {
        val userCall = retrofitIns.service.createUser(user)
        userCall?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                onFinishedListener!!.onFinished(response?.isSuccessful!!, response.code())

            }

        })
    }





}