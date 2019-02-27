package de.codecrops.vertretungsplangymwen.gui.customFragments


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import de.codecrops.vertretungsplangymwen.ClockSettingsActivity
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.refresh.RefreshManager
import de.codecrops.vertretungsplangymwen.settings.SettingsSPAdapter
import java.lang.Exception

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var lastActionBarTitle: String
private val LOG_TAG = "SettingsRefrFr"

class SettingsRefreshFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //Verbinden mit SPAdapter
        preferenceManager.preferenceDataStore = SettingsSPAdapter(context!!)
        //Setzen des Inhalts
        setPreferencesFromResource(R.xml.settings_refresh, rootKey)

        //Registrieren eines onClickListeners für die ClockSettingsActivity (da ein spezielles Intent programmiert werden muss)
        val clocksettings = findPreference("clocksettings")
        clocksettings.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(context, ClockSettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivity(intent)

            true
        }
    }

    override fun onStart() {
        //Speichert den Titel der Actionbar ab, bevor dieser geändert wird
        try {
            val act : AppCompatActivity = activity as AppCompatActivity
            lastActionBarTitle = act.supportActionBar!!.title.toString()
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Speichern des Titels der letzten Actionbar fehlgeschlagen!")
        }

        //Setzt den Titel der Actionbar, um auf das Fragment zu passen
        try {
            val act : AppCompatActivity = activity as AppCompatActivity
            //act.supportActionBar!!.setIcon(R.drawable.ic_refresh_black)
            act.supportActionBar!!.title = "Hintergrundaktualisierung"
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Setzen des Titels der  Actionbar fehlgeschlagen!")
        }
        super.onStart()
    }

    override fun onPause() {
        try {
            val act : AppCompatActivity = activity as AppCompatActivity
            act.supportActionBar!!.title = lastActionBarTitle
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Zurücksetzen des Titels der letzten Actionbar fehlgeschlagen!")
        }
        super.onPause()
    }
}
