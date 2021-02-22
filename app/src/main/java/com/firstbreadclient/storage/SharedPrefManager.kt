package com.firstbreadclient.storage

import android.content.Context

class SharedPrefManager private constructor(private val mContext: Context) {
    fun saveCntkod(cntkod: String?) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("cntkod", cntkod)
        editor.apply()
    }

    val cntkod: String?
        get() {
            val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("cntkod", "")
        }

    fun savePassword(password: String?) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("password", password)
        editor.apply()
    }

    val password: String?
        get() {
            val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("password", "")
        }

    fun saveToken(token: String?) {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    val token: String?
        get() {
            val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("token", "")
        }
    val isLoggedIn: Boolean
        get() {
            val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getInt("id", -1) != -1
        }

    fun clear() {
        val sharedPreferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        private const val SHARED_PREF_NAME = "shared_preff"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(context: Context): SharedPrefManager? {
            if (mInstance == null) {
                mInstance = SharedPrefManager(context)
            }
            return mInstance
        }
    }
}