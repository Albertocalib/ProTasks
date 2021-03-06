package com.example.protasks.presenters.login

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.protasks.RetrofitInstance
import com.example.protasks.restServices.UserRestService
import com.example.protasks.models.User
import com.example.protasks.utils.Preference
import com.example.protasks.views.ILoginView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginPresenterImp(private var iLoginView: ILoginView, private var context: Context) :
    ILoginPresenter {
    private var handler: Handler = Handler(Looper.getMainLooper())
    private val retrofitIns:RetrofitInstance<UserRestService> = RetrofitInstance("api/user/",UserRestService::class.java)
    private val preference:Preference = Preference()
    override fun doLogin(userName: String, password: String, keep_login: Boolean) {
        val user = retrofitIns.service.logInAttempt(userName, password)
        user.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.v("retrofit", t.toString())
            }

            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                if (response?.isSuccessful!!){
                    preference.saveKeepLogin(keep_login,context)
                    preference.saveEmail(userName,context)
                }
                handler.postDelayed({ iLoginView.onLoginResult(response.isSuccessful, response.code()) },0)

            }

        })
    }

    override fun setProgressBarVisiblity(visiblity: Int) {
        iLoginView.onSetProgressBarVisibility(visiblity)
    }

    override fun cheekKeepLogin() {
        if (preference.getKeepLogin(context)!!){
            iLoginView.goToMainactivity()
        }
    }


}