package com.example.protasks.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.protasks.R
import com.example.protasks.presenters.register.IRegisterContract
import com.example.protasks.presenters.register.RegisterPresenterImp


class RegisterActivity : AppCompatActivity(), IRegisterContract.View, View.OnClickListener {
    private var editUser: EditText? = null
    private var editPass: EditText? = null
    private var editEmail: EditText? = null
    private var editName: EditText? = null
    private var editSurname: EditText? = null
    private var editPassRepeat: EditText? = null
    private var btnRegister: Button? = null
    private var registerPresenter: IRegisterContract.Presenter? = null
    private var progressBar: ProgressBar? = null
    private val handler:Handler= Handler(Looper.myLooper()!!)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //find view
        editUser = findViewById<View>(R.id.usernameText) as EditText
        editPass = findViewById<View>(R.id.password) as EditText
        editPassRepeat = findViewById<View>(R.id.passwordRepeat) as EditText
        editEmail = findViewById<View>(R.id.email) as EditText
        editName = findViewById<View>(R.id.nameText) as EditText
        editSurname = findViewById<View>(R.id.surnameText) as EditText
        editPassRepeat = findViewById<View>(R.id.passwordRepeat) as EditText
        btnRegister =
            findViewById<View>(R.id.registerButton) as Button
        progressBar = findViewById<View>(R.id.progress_register) as ProgressBar
        //set listener
        btnRegister!!.setOnClickListener(this)
        val watcherPassword: TextWatcher = object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                //I dont want to do anything on text changed
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                //I dont want to do anything before text changed
            }

            override fun afterTextChanged(s: Editable) {
                registerPresenter!!.checkPassword(editPass!!.text.toString(),editPassRepeat!!.text.toString())
            }
        }
        editPassRepeat!!.addTextChangedListener(watcherPassword)
        editPass!!.addTextChangedListener(watcherPassword)

        editEmail!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                //I dont want to do anything on text changed
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                //I dont want to do anything before text changed
            }

            override fun afterTextChanged(s: Editable) {
                registerPresenter!!.checkEmail(editEmail!!.text.toString())
            }
        })
        //init
        registerPresenter = RegisterPresenterImp(this)
        registerPresenter!!.setProgressBarVisiblity(View.INVISIBLE)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.registerButton -> {
                registerPresenter!!.setProgressBarVisiblity(View.VISIBLE)
                btnRegister!!.isEnabled = false
                registerPresenter!!.createUser(editName!!.text.toString(), editSurname!!.text.toString(),
                    editUser!!.text.toString(),editPass!!.text.toString(),editEmail!!.text.toString())
            }
        }
    }


    override fun onRegisterResult(result: Boolean?, code: Int) {
        registerPresenter!!.setProgressBarVisiblity(View.INVISIBLE)
        btnRegister!!.isEnabled = true
        if (result!!) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Register Fail, because the user or the email already exists" , Toast.LENGTH_LONG).show()
    }


    override fun onSetProgressBarVisibility(visibility: Int) {
        progressBar!!.visibility = visibility
    }

    override fun postResult(successful: Boolean?, code: Int) {
        handler.postDelayed({onRegisterResult(successful, code)},0)
    }

    override fun onResponseFailure(t: Throwable?) {
        Log.e("REGISTER", t!!.message!!)
        Toast.makeText(this, getString(R.string.communication_error), Toast.LENGTH_LONG).show()

    }

    override fun passwordError(message: String) {
        editPassRepeat!!.error = message
    }
    override fun emailError(message: String) {
        editEmail!!.error = message
    }

}

