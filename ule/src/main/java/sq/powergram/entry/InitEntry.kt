package sq.powergram.entry

import android.app.AndroidAppHelper
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import sq.powergram.hook.PrivacyHooks

class InitEntry : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "org.telegram.messenger") return

        Log.d("PowerGram", "Injecting hooks into Telegram")

        // فراخوانی تمام hookهای ماژول
        PrivacyHooks.apply(lpparam)
    }
}