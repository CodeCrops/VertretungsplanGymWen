package de.codecrops.vertretungsplangymwen.gui.customFragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import de.codecrops.vertretungsplangymwen.R

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private val LOG_TAG = "SettingsAutoClock"

//is bugged! Dont use without fixing

class SettingsAutoClockFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedStateInstance: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_auto_clock, rootKey)
    }
}
