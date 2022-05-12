package com.example.protasks.presenters.login

import com.example.protasks.utils.RetrofitInstance
import com.example.protasks.restServices.UserRestService
import com.example.protasks.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.protasks.presenters.login.ILoginContract.Model.OnFinishedListener


class LoginModel:ILoginContract.Model{
    private val retrofitIns: RetrofitInstance<UserRestService> = RetrofitInstance("api/user/",UserRestService::class.java)
    override fun doLogin(onFinishedListener: OnFinishedListener?, userName: String, password: String) {
        val user = retrofitIns.service.logInAttempt(userName, password)
        user.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                onFinishedListener!!.onFailure(t)
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                var userResp:User? = null
                if (response?.isSuccessful!!){
                    userResp = response.body()!!
                }
                onFinishedListener!!.onFinished(response.isSuccessful,response.code(),userResp)
            }

        })
    }



}