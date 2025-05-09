package com.sq.powergram.hooks

import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.content.Context

object SettingManager {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun set(key: String, value: Boolean) {
        prefs?.edit()?.putBoolean(key, value)?.apply()
    }

    fun get(key: String): Boolean {
        return prefs?.getBoolean(key, false) ?: false
    }
}