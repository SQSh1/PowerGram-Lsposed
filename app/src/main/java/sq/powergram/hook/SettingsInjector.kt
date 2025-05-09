package sq.powergram.hook

import android.content.Intent
import android.view.View
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

object SettingsInjector {

    fun injectPrivacySettingsButton(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            XposedHelpers.findAndHookMethod(
                "org.telegram.ui.SettingsActivity",
                lpparam.classLoader,
                "onFragmentCreate",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val fragment = param.thisObject
                        val context = XposedHelpers.callMethod(fragment, "getContext") as android.content.Context

                        // ساختن ویو دکمه جدید
                        val textViewClass = XposedHelpers.findClass("org.telegram.ui.Cells.TextSettingsCell", lpparam.classLoader)
                        val newItem = XposedHelpers.newInstance(textViewClass, context)

                        XposedHelpers.callMethod(newItem, "setText", "تنظیمات حریم خصوصی")

                        (newItem as View).setOnClickListener {
                            val intent = Intent(context, Class.forName("sq.powergram.ui.SettingsActivity"))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }

                        // گرفتن لیست و افزودن آیتم
                        val listView = XposedHelpers.getObjectField(fragment, "listView") as? android.widget.ListView
                        listView?.addFooterView(newItem)
                    }
                }
            )
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}
