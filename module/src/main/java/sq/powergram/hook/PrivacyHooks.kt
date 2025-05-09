package sq.powergram.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PrivacyHooks {

    fun apply(lpparam: XC_LoadPackage.LoadPackageParam) {
        hideReadReceipts(lpparam)
        hideTypingStatus(lpparam)
        hideOnlineStatus(lpparam)
    }

    private fun hideReadReceipts(lpparam: XC_LoadPackage.LoadPackageParam) {
        // Hook برای جلوگیری از ارسال وضعیت «دیده شده»
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
        // Hook برای جلوگیری از ارسال وضعیت «در حال تایپ»
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
        // (در آینده) Hook برای «نمایش آفلاین بودن دائم»
        // TODO: بررسی و تست بیشتر نیاز است
    }
}
package sq.powergram.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PrivacyHooks {

    fun apply(lpparam: XC_LoadPackage.LoadPackageParam) {
        showDeletedMessages(lpparam)
        // فیچرهای دیگر اینجا اضافه خواهند شد
    }

    private fun showDeletedMessages(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod(
                "org.telegram.messenger.MessageObject",
                lpparam.classLoader,
                "isOut",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        // فقط پیام‌های ورودی بررسی بشن
                        val result = param.result as Boolean
                        if (!result) {
                            // اینجا میشه وضعیت پیام رو طوری دستکاری کرد که حذف نشه
                            XposedHelpers.setBooleanField(param.thisObject, "deleted", false)
                        }
                    }
                }
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}