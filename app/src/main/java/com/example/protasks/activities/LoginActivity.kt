package com.example.protasks.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.protasks.R
import com.example.protasks.presenters.login.ILoginPresenter
import com.example.protasks.presenters.login.LoginPresenterImp
import com.example.protasks.views.ILoginView


class LoginActivity : AppCompatActivity(), ILoginView, View.OnClickListener {
    private var editUser: EditText? = null
    private var editPass: EditText? = null
    private var btnLogin: Button? = null
    private var linkSignUp: TextView? = null
    private var loginPresenter: ILoginPresenter? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //find view
        editUser = findViewById<View>(R.id.emailLogin) as EditText
        editPass = findViewById<View>(R.id.passwordText) as EditText
        linkSignUp = findViewById<View>(R.id.link_signup) as TextView
        btnLogin =
            findViewById<View>(R.id.loginButton) as Button
        progressBar = findViewById<View>(R.id.progress_login) as ProgressBar
        //set listener
        btnLogin!!.setOnClickListener(this)
        //init
        loginPresenter =
            LoginPresenterImp(this)
        loginPresenter!!.setProgressBarVisiblity(View.INVISIBLE)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> {
                loginPresenter!!.setProgressBarVisiblity(View.VISIBLE)
                btnLogin!!.isEnabled = false
                loginPresenter!!.doLogin(editUser!!.text.toString(), editPass!!.text.toString())
            }
            R.id.link_signup ->{
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
}

