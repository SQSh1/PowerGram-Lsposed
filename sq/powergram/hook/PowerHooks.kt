private fun getFeatureEnabled(
    lpparam: XC_LoadPackage.LoadPackageParam,
    key: String,
    defaultValue: Boolean = true
): Boolean {
    return try {
        val appContext = Class
            .forName("android.app.ActivityThread")
            .getMethod("currentApplication")
            .invoke(null) as Context

        val prefsContext = appContext.createPackageContext(
            "sq.powergram.settings", Context.CONTEXT_IGNORE_SECURITY
        )

        val prefs = prefsContext.getSharedPreferences(
            "powergram_privacy_prefs", Context.MODE_PRIVATE
        )

        prefs.getBoolean(key, defaultValue)
    } catch (e: Throwable) {
        e.printStackTrace()
        defaultValue
    }
}
