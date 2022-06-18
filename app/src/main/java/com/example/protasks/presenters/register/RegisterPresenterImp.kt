package com.example.protasks.presenters.register

import com.example.protasks.models.User
import java.util.regex.Pattern


class RegisterPresenterImp(private var iRegisterView: IRegisterContract.View) :
    IRegisterContract.Presenter, IRegisterContract.Model.OnFinishedListener {
    private val registerModel: RegisterModel = RegisterModel()

    override fun createUser(
        name: String,
        surname: String,
        userName: String,
        password: String,
        email: String
    ) {
        val newUser = User(name, surname, userName, password, email)
        registerModel.createUser(this, newUser)
    }

    override fun setProgressBarVisiblity(visiblity: Int) {
        iRegisterView.onSetProgressBarVisibility(visiblity)
    }

    override fun checkEmail(email: String): Boolean {
        val anyChar =
            "^([^\\s()<>,:;\\[\\]Çç%&@á-źÁ-Ź]+@[^\\s()<>,:;\\[\\]Çç%&@á-źÁ-Ź+.]+(\\.[^\\s()<>,:;\\[\\]Çç%&@á-źÁ-Ź+.]+)+)$"
        if (!Pattern.compile(anyChar).matcher(email).matches()) {
            iRegisterView.emailError("This is not a valid e-mail (example@example.com)")
            return false
        }
        return true
    }

    override fun checkPassword(password: String, passwordRep: String): Boolean {
        if (password != "" && passwordRep != password) {
            iRegisterView.passwordError("Passwords doesn't match")
            return false
        }
        return true
    }

    override fun onFinished(successful: Boolean, code: Int) {
        iRegisterView.postResult(successful, code)
    }

    override fun onFailure(t: Throwable?) {
        iRegisterView.onResponseFailure(t)
    }


}