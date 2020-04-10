package com.example.protasks.presenters.register

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.protasks.RetrofitInstance
import com.example.protasks.restServices.UserRestService
import com.example.protasks.models.User
import com.example.protasks.views.IRegisterView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class RegisterPresenterImp(private var iRegisterView: IRegisterView) :
    IRegisterPresenter {
    private var handler: Handler
    private val retrofitIns: RetrofitInstance<UserRestService> = RetrofitInstance("api/user/",UserRestService::class.java)

    override fun createUser(name:String,surname:String,userName: String, password: String,email:String) {
        val newUser=User(name,surname,userName,password,email)
        val user = retrofitIns.service.createUser(newUser)
        user?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                handler.postDelayed({ iRegisterView.onRegisterResult(response?.isSuccessful, response!!.code()) },0)

            }

        })
    }

    override fun setProgressBarVisiblity(visiblity: Int) {
        iRegisterView.onSetProgressBarVisibility(visiblity)
    }

    override fun checkEmail(email: String):Boolean {
        val anyChar="^([^\\s()<>,:;\\[\\]Çç%&@á-źÁ-Ź]+@[^\\s()<>,:;\\[\\]Çç%&@á-źÁ-Ź+.]+(\\.[^\\s()<>,:;\\[\\]Çç%&@á-źÁ-Ź+.]+)+)$"
        return Pattern.compile(anyChar).matcher(email).matches()
    }


    init {
        handler = Handler(Looper.getMainLooper())
    }
}