package com.example.protasks.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.protasks.models.Board
import com.example.protasks.models.BoardUsersPermRel
import com.example.protasks.models.User

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