package com.example.protasks.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.protasks.R
import com.example.protasks.presenters.login.ILoginContract
import com.example.protasks.presenters.login.LoginPresenterImp
import com.example.protasks.utils.Preference
import com.example.protasks.utils.PreferencesManager


class LoginActivity : AppCompatActivity(), ILoginContract.View, View.OnClickListener {
    private var editUser: EditText? = null
    private var editPass: EditText? = null
    private var btnLogin: Button? = null
    private var linkSignUp: TextView? = null
    private var loginPresenter: LoginPresenterImp? = null
    private var progressBar: ProgressBar? = null
    private var keepLogin: CheckBox? = null
    private val handler: Handler = Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //find view
        editUser = findViewById(R.id.emailLogin)
        editPass = findViewById(R.id.passwordText)
        keepLogin = findViewById(R.id.checkBox)
        linkSignUp = findViewById(R.id.link_signup)
        btnLogin = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progress_login)
        //set listener
        btnLogin!!.setOnClickListener(this)
        //init
        val preference:PreferencesManager = Preference(baseContext)
        loginPresenter = LoginPresenterImp(this, preference)
        loginPresenter!!.setProgressBarVisiblity(View.INVISIBLE)
        loginPresenter!!.cheekKeepLogin()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> {
                loginPresenter!!.setProgressBarVisiblity(View.VISIBLE)
                btnLogin!!.isEnabled = false
                loginPresenter!!.doLogin(getUserName(), getPassword(),keepLogin!!.isChecked)
            }
            R.id.link_signup -> {
                goToSignUpActivity()
            }
        }
    }

    override fun onLoginResult(result: Boolean?, code: Int) {
        loginPresenter!!.setProgressBarVisiblity(View.INVISIBLE)
        btnLogin!!.isEnabled = true
        if (result!!) {
            goToMainactivity()
            Toast.makeText(this, "Login Success", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Login Fail, code = $code", Toast.LENGTH_SHORT).show()
    }


    override fun onSetProgressBarVisibility(visibility: Int) {
        progressBar!!.visibility = visibility
    }

    override fun goToSignUpActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun goToMainactivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun sendLoginResult(successful: Boolean, code: Int) {
        handler.postDelayed({ onLoginResult(successful,code) },0)
    }

    override fun onResponseFailure(t: Throwable?) {
        Log.e("LOGIN", t!!.message!!)
        Toast.makeText(this, getString(R.string.communication_error), Toast.LENGTH_LONG).show()
    }

    override fun getUserName():String {
        return editUser!!.text.toString()
    }

    override fun getPassword():String {
        return editPass!!.text.toString()
    }
}

