package de.codecrops.vertretungsplangymwen.gui.customFragments

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import de.codecrops.vertretungsplangymwen.R
import java.lang.Exception

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private val LOG_TAG = "SettingsHeadersFr"

class SettingsHeadersFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //Setzen des Inhalts
        setPreferencesFromResource(R.xml.settings_headers, rootKey)
    }

    override fun onStart() {
        //Setzt den Titel der Actionbar, um auf das Fragment zu passen
        try {
            val act : AppCompatActivity = activity as AppCompatActivity
            //act.supportActionBar!!.setIcon(R.drawable.ic_baseline_settings_20px)
            act.supportActionBar!!.title = "Einstellungen"
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Setzen des Titels der  Actionbar fehlgeschlagen!")
        }
        super.onStart()
    }


}