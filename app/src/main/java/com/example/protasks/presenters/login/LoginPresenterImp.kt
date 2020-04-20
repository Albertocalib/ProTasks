package com.example.protasks.presenters.login

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.protasks.RetrofitInstance
import com.example.protasks.restServices.UserRestService
import com.example.protasks.models.User
import com.example.protasks.views.ILoginView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginPresenterImp(private var iLoginView: ILoginView) :
    ILoginPresenter {
    private var handler: Handler
    private val retrofitIns:RetrofitInstance<UserRestService> = RetrofitInstance("api/user/",UserRestService::class.java)

    override fun doLogin(userName: String, password: String) {
        val user = retrofitIns.service.logInAttempt(userName, password)
        user.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                handler.postDelayed({ iLoginView.onLoginResult(response?.isSuccessful, response!!.code()) },0)

            }

        })
    }

    override fun setProgressBarVisiblity(visiblity: Int) {
        iLoginView.onSetProgressBarVisibility(visiblity)
    }



    init {
        handler = Handler(Looper.getMainLooper())
    }
}