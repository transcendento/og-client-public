package com.firstbreadclient.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferencesManager {
    private const val SHARED_PREF_NAME = "com.firstbreadclient.SHARED_PREF_KEY"
    private const val SHARED_PREF_MODE = Context.MODE_PRIVATE
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, SHARED_PREF_MODE)
    }

    var cntkod: String?
        get() = sharedPreferences.getString("cntkod", null)
        set(value) = sharedPreferences.edit {
            putString("cntkod", value)
        }

    var password: String?
        get() = sharedPreferences.getString("password", null)
        set(value) = sharedPreferences.edit { putString("password", value) }

    var token: String?
        get() = sharedPreferences.getString("token", null)
        set(value) = sharedPreferences.edit { putString("token", value) }

    var daysId: String?
        get() = sharedPreferences.getString("daysid", null)
        set(value) = sharedPreferences.edit { putString("daysid", value) }

    val isLoggedIn: Boolean
        get() = sharedPreferences.getInt("id", -1) != -1



    fun clear() {
        sharedPreferences.edit { clear() }
    }
}