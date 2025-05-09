package sq.powergram.util

import android.content.Context
import android.content.SharedPreferences

object SettingsManager {
    private const val PREF_NAME = "powergram_privacy_prefs"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isFeatureEnabled(key: String, defaultValue: Boolean = true): Boolean {
        return prefs.getBoolean(key, defaultValue)
    }

    fun setFeatureEnabled(key: String, enabled: Boolean) {
        prefs.edit().putBoolean(key, enabled).apply()
    }
}