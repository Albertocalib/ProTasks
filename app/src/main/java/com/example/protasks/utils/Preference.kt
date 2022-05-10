package com.example.protasks.utils

import android.content.Context
import androidx.preference.PreferenceManager

class Preference(private val context:Context) {

    fun saveEmail(email: String?): Boolean {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(Constants.KEY_EMAIL, email)
        prefsEditor.apply()
        return true
    }

    fun getEmail(): String? {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(Constants.KEY_EMAIL, null)
    }

    fun saveKeepLogin(keepLogin: Boolean) {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(Constants.KEY_KEEP_LOGIN, keepLogin)
        prefsEditor.apply()
    }

    fun getKeepLogin(): Boolean {
        val prefs =PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(Constants.KEY_KEEP_LOGIN, false)
    }

    fun getPrefViewMode(): Boolean {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(Constants.KEY_BOARDS_LIST_VIEW, false)
    }
    fun removePreferences() {
       PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()    }

    fun setPrefViewMode(listMode: Boolean) {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(Constants.KEY_BOARDS_LIST_VIEW, listMode)
        prefsEditor.apply()
    }
    fun setModeView(listMode: Boolean, context: Context?){
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context!!)
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(Constants.MODE_VIEW, listMode)
        prefsEditor.apply()
    }
    fun getModeView():Boolean {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(Constants.MODE_VIEW, false)
    }
}