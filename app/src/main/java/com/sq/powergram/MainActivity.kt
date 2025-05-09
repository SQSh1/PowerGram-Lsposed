package com.sq.powergram

import android.os.Bundle
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val checkSeenPrivate = findViewById<CheckBox>(R.id.check_seen_private)
        checkSeenPrivate.setOnCheckedChangeListener { _, isChecked ->
            SettingManager.set("hide_seen_private", isChecked)
        }

        // سایر چک‌باکس‌ها اینجا اضافه خواهند شد
    }
}