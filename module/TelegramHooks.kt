package com.sq.powergram.hooks

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XC_MethodHook

class TelegramHooks : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!lpparam.packageName.contains("org.telegram.messenger")) return

        if (SettingManager.get("hide_online")) {
            try {
                val userClass = XposedHelpers.findClass(
                    "org.telegram.tgnet.TLRPC$User", lpparam.classLoader
                )
                XposedHelpers.findAndHookMethod(userClass, "status", object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        param.result = null
                    }
                })
            } catch (e: Throwable) {
                XposedBridge.log("PowerGram Hook Error: $e")
            }
        }
    }
}