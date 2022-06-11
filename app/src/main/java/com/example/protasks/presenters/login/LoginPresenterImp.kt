package com.example.protasks.presenters.login

import com.example.protasks.models.User
import com.example.protasks.utils.PreferencesManager


class LoginPresenterImp(private var iLoginView: ILoginContract.View, private val preference:PreferencesManager) :
    ILoginContract.Presenter,ILoginContract.Model.OnFinishedListener {

    private var keepLogin:Boolean = false
    private val loginModel: LoginModel = LoginModel()

    override fun doLogin(userName: String, password: String, keepLogin: Boolean) {
        this.keepLogin = keepLogin
        loginModel.doLogin(this,userName,password)
    }

    override fun setProgressBarVisiblity(visiblity: Int) {
        iLoginView.onSetProgressBarVisibility(visiblity)
    }

    override fun cheekKeepLogin() {
        if (preference.getKeepLogin()){
            iLoginView.goToMainactivity()
        }
    }

    override fun onFinished(successful: Boolean, code: Int, user:User?) {
        if (successful){
            preference.saveKeepLogin(keepLogin)
            preference.saveEmail(user!!.getEmail())
        }
        iLoginView.onLoginResult(successful,code)
    }

    override fun onFailure(t: Throwable?) {
        iLoginView.onResponseFailure(t)
    }


}