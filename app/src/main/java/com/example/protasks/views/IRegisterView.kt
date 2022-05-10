package com.example.protasks.views

interface IRegisterView {
    fun onRegisterResult(result: Boolean?, code: Int)
    fun onSetProgressBarVisibility(visibility: Int)
    fun postResult(successful: Boolean?, code: Int)
}