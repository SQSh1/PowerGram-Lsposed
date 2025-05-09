package sq.powergram.hook

import android.content.Context
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PrivacyHooks {

    fun apply(lpparam: XC_LoadPackage.LoadPackageParam) {
        hideReadReceipts(lpparam)
        hideTypingStatus(lpparam)
        hideOnlineStatus(lpparam)
        showDeletedMessages(lpparam)
        hideStoryViewStatus(lpparam)
        removeForwardQuote(lpparam)
    }

    private fun hideReadReceipts(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!getFeatureEnabled(lpparam, "hide_read_receipts")) return

        XposedHelpers.findAndHookMethod(
            "org.telegram.messenger.MessagesController",
            lpparam.classLoader,
            "markDialogAsRead",
            Long::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.result = null
                }
            }
        )
    }

    private fun hideTypingStatus(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!getFeatureEnabled(lpparam, "hide_typing_status")) return

        XposedHelpers.findAndHookMethod(
            "org.telegram.messenger.SendMessagesHelper",
            lpparam.classLoader,
            "sendTyping",
            Long::class.javaPrimitiveType,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.result = null
                }
            }
        )
    }

    private fun hideOnlineStatus(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!getFeatureEnabled(lpparam, "hide_online_status")) return

        // TODO: Hook دقیق برای مخفی کردن وضعیت آنلاین
        // فعلاً به‌صورت TODO گذاشته شده
    }

    private fun showDeletedMessages(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!getFeatureEnabled(lpparam, "show_deleted_messages")) return

        try {
            XposedHelpers.findAndHookMethod(
                "org.telegram.messenger.MessageObject",
                lpparam.classLoader,
                "isOut",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val result = param.result as Boolean
                        if (!result) {
                            XposedHelpers.setBooleanField(param.thisObject, "deleted", false)
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun hideStoryViewStatus(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!getFeatureEnabled(lpparam, "hide_story_view_status")) return

        try {
            XposedHelpers.findAndHookMethod(
                "org.telegram.messenger.StoriesController",
                lpparam.classLoader,
                "markStoryAsViewed",
                Long::class.javaPrimitiveType,
                Int::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType,
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                        param.result = null
                    }
                }
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun removeForwardQuote(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!getFeatureEnabled(lpparam, "remove_forward_quote")) return

        try {
            XposedHelpers.findAndHookMethod(
                "org.telegram.messenger.MessageObject",
                lpparam.classLoader,
                "isForwarded",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        param.result = false
                    }
                }
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun getFeatureEnabled(
        lpparam: XC_LoadPackage.LoadPackageParam,
        key: String,
        defaultValue: Boolean = true
    ): Boolean {
        return try {
            val context = lpparam.appInfo?.packageName?.let {
                val settingsContext = lpparam.classLoader
                    .loadClass("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as? Context
                settingsContext?.createPackageContext("sq.powergram.settings", Context.MODE_PRIVATE)
            }

            val prefs = context?.getSharedPreferences("powergram_privacy_prefs", Context.MODE_PRIVATE)
            prefs?.getBoolean(key, defaultValue) ?: defaultValue
        } catch (e: Throwable) {
            e.printStackTrace()
            defaultValue
        }
    }
}
