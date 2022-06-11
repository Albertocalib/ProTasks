package com.example.protasks.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class Preference(context:Context):PreferencesManager {

    var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun saveEmail(email: String?): Boolean {
        val prefsEditor = prefs.edit()
        prefsEditor.putString(Constants.KEY_EMAIL, email)
        prefsEditor.apply()
        return true
    }

    override fun getEmail(): String? {
        return prefs.getString(Constants.KEY_EMAIL, null)
    }

    override fun saveKeepLogin(keepLogin: Boolean) {
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(Constants.KEY_KEEP_LOGIN, keepLogin)
        prefsEditor.apply()
    }

    override fun getKeepLogin(): Boolean {
        return prefs.getBoolean(Constants.KEY_KEEP_LOGIN, false)
    }

    override fun getPrefViewMode(): Boolean {
        return prefs.getBoolean(Constants.KEY_BOARDS_LIST_VIEW, false)
    }
    override fun removePreferences() {
       prefs.edit().clear().apply()
    }

    override fun setPrefViewMode(listMode: Boolean) {
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(Constants.KEY_BOARDS_LIST_VIEW, listMode)
        prefsEditor.apply()
    }
    override fun setModeView(listMode: Boolean){
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(Constants.MODE_VIEW, listMode)
        prefsEditor.apply()
    }
    override fun getModeView():Boolean {
        return prefs.getBoolean(Constants.MODE_VIEW, false)
    }
}