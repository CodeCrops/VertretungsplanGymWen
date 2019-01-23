package de.codecrops.vertretungsplangymwen.gui.customFragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import de.codecrops.vertretungsplangymwen.R

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsHeadersFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //Setzen des Inhalts
        setPreferencesFromResource(R.xml.settings_headers, rootKey)
    }
}