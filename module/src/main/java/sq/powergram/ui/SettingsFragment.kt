package sq.powergram.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sq.powergram.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs_privacy)
    }
}
