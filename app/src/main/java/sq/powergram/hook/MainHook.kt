object MainHook {
    fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "org.telegram.messenger") {
            PrivacyHooks.apply(lpparam)
            SettingsInjector.injectPrivacySettingsButton(lpparam)
        }
    }
}
