package de.codecrops.vertretungsplangymwen.gui.customFragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.settings.SettingsManager
import de.codecrops.vertretungsplangymwen.settings.SettingsSPAdapter

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsNotificationFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedStateInstance: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = SettingsSPAdapter(context!!)
        setPreferencesFromResource(R.xml.settings_notifications, rootKey)
    }




}
