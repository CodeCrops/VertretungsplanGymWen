package de.codecrops.vertretungsplangymwen.gui.customFragments


import android.os.Bundle
import android.support.v14.preference.PreferenceFragment
import android.support.v7.preference.PreferenceFragmentCompat
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.settings.SettingsSPAdapter

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsRefreshFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = SettingsSPAdapter(context!!)
        setPreferencesFromResource(R.xml.settings_refresh, rootKey)
    }

}
