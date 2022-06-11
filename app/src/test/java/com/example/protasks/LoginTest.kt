package com.example.protasks

import com.example.protasks.presenters.login.ILoginContract
import com.example.protasks.presenters.login.LoginPresenterImp
import com.example.protasks.utils.Preference
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import android.R

import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class LoginTest{

    var loginPresenter:ILoginContract.Presenter?=null
    @Mock
    lateinit var loginView:ILoginContract.View

    @Mock
    lateinit var preference:Preference


    @Before
    fun setup(){
        loginPresenter = LoginPresenterImp(loginView,preference)
    }

    @Test
    fun cheekKeepLoginTestTrue() {
        `when`(preference.getKeepLogin()).thenReturn(true)
        loginPresenter!!.cheekKeepLogin()
        Mockito.verify(loginView,Mockito.times(1)).goToMainactivity()

    }
    @Test
    fun cheekKeepLoginTestFalse() {
        `when`(preference.getKeepLogin()).thenReturn(false)
        loginPresenter!!.cheekKeepLogin()
        verify(loginView,Mockito.never()).goToMainactivity()

    }

    @Test
    fun shouldShowErrorWhenUsernameIsEmpty() {
        `when`(loginView.getUserName()).thenReturn("")
        `when`(loginView.getPassword()).thenReturn("ABC")

        loginPresenter!!.doLogin(loginView.getUserName(),loginView.getPassword(),true)
        verify(loginView,Mockito.never()).goToMainactivity()
    }

}
