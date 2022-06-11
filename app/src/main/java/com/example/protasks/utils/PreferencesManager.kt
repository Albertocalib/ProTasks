package com.example.protasks.utils

interface PreferencesManager {
    fun saveEmail(email: String?): Boolean
    fun getEmail(): String?
    fun saveKeepLogin(keepLogin: Boolean)
    fun getKeepLogin(): Boolean
    fun getPrefViewMode(): Boolean
    fun removePreferences()
    fun setPrefViewMode(listMode: Boolean)
    fun setModeView(listMode: Boolean)
    fun getModeView():Boolean
}