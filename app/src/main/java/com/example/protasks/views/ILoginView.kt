package com.example.protasks.views

interface ILoginView {
    fun onLoginResult(result: Boolean?, code: Int)
    fun onSetProgressBarVisibility(visibility: Int)
    fun goToSignUpActivity()
    fun goToMainactivity()
    fun sendLoginResult(successful: Boolean, code: Int)
}