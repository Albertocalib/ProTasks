package com.example.protasks

import com.example.protasks.presenters.login.ILoginContract
import com.example.protasks.presenters.login.LoginPresenterImp
import com.example.protasks.utils.PreferencesManager
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.*
import org.mockito.junit.MockitoJUnitRunner
import com.example.protasks.models.User
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class LoginPresenterTest{

    var loginPresenter:ILoginContract.Presenter?=null
    @Mock
    lateinit var loginView:ILoginContract.View

    @Mock
    lateinit var preference:PreferencesManager


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

    @Test
    fun doOnFinishedNoSuccessful() {
        val user=User("","","","","")
        loginPresenter!!.onFinished(false,404,user)
        verify(preference,Mockito.never()).saveKeepLogin(true)
        verify(preference,Mockito.never()).saveKeepLogin(false)
        Mockito.verify(loginView,Mockito.times(1)).onLoginResult(false,404)

    }
    @Test
    fun doOnFinishedSuccessful() {
        val user=User("","","","","acl@g.c")
        loginPresenter!!.onFinished(true,200,user)
        verify(preference,Mockito.times(1)).saveKeepLogin(false)
        verify(preference,Mockito.times(1)).saveEmail("acl@g.c")
        Mockito.verify(loginView,Mockito.times(1)).onLoginResult(true,200)

    }
    @Test
    fun doOnFailure() {
        val t= Throwable("Error")
        loginPresenter!!.onFailure(t)
        verify(loginView,Mockito.times(1)).onResponseFailure(t)
    }
}
