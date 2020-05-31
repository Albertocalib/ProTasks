package com.example.protasks.utils

import android.content.Context
import androidx.preference.PreferenceManager

class Preference {

    fun saveEmail(email: String?, context: Context?): Boolean {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putString(Constants.KEY_EMAIL, email)
        prefsEditor.apply()
        return true
    }

    fun getEmail(context: Context?): String? {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(Constants.KEY_EMAIL, null)
    }

    fun saveKeepLogin(keep_login: Boolean, context: Context?) {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(Constants.KEY_KEEP_LOGIN, keep_login)
        prefsEditor.apply()
    }

    fun getKeepLogin(context: Context?): Boolean? {
        val prefs =PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(Constants.KEY_KEEP_LOGIN, false)
    }

    fun getPrefViewMode(context: Context?): Boolean? {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(Constants.KEY_BOARDS_LIST_VIEW, false)
    }
    fun removePreferences(context: Context?) {
       PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()    }

    fun setPrefViewMode(listMode: Boolean, context: Context?) {
        val prefs =
            PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor = prefs.edit()
        prefsEditor.putBoolean(Constants.KEY_BOARDS_LIST_VIEW, listMode)
        prefsEditor.apply()
    }
}