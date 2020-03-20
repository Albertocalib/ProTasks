package com.example.protasks.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.protasks.R
import com.example.protasks.presenters.register.IRegisterPresenter
import com.example.protasks.presenters.register.RegisterPresenterImp
import com.example.protasks.views.IRegisterView


class RegisterActivity : AppCompatActivity(), IRegisterView, View.OnClickListener {
    private var editUser: EditText? = null
    private var editPass: EditText? = null
    private var editEmail: EditText? = null
    private var editName: EditText? = null
    private var editSurname: EditText? = null
    private var editPassRepeat: EditText? = null
    private var btnRegister: Button? = null
    private var registerPresenter: IRegisterPresenter? = null
    private var progressBar: ProgressBar? = null

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
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (editPass!!.text.toString() !="" && editPassRepeat!!.text.toString()!=editPass!!.text.toString()){
                    editPassRepeat!!.error = "Passwords doesn't match"
                }
            }
        }
        editPassRepeat!!.addTextChangedListener(watcherPassword)
        editPass!!.addTextChangedListener(watcherPassword)

        editEmail!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                if (!registerPresenter!!.checkEmail(editEmail!!.text.toString())){
                    editEmail!!.error = "This is not a valid e-mail (example@example.com)"
                }
            }
        })
        //init
        registerPresenter =
            RegisterPresenterImp(this)
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
            startActivity(intent);
            Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Register Fail, because the user or the email already exists" , Toast.LENGTH_LONG).show()
    }


    override fun onSetProgressBarVisibility(visibility: Int) {
        progressBar!!.visibility = visibility
    }
}

