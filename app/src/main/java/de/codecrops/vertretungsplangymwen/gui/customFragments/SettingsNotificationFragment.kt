package de.codecrops.vertretungsplangymwen.gui.customFragments


import android.os.Bundle
import android.support.v14.preference.SwitchPreference
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.settings.SettingsSPAdapter
import java.lang.Exception

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var lastActionBarTitle: String
private val LOG_TAG = "SettingsNotFr"

class SettingsNotificationFragment : PreferenceFragmentCompat() {

    lateinit var master : SwitchPreference

    override fun onCreatePreferences(savedStateInstance: Bundle?, rootKey: String?) {
        //Verbinden mit SPAdapter
        preferenceManager.preferenceDataStore = SettingsSPAdapter(context!!)
        //Setzen des Inhalts
        setPreferencesFromResource(R.xml.settings_notifications, rootKey)
        //finden und speichern der Preferenzen
        //master = findPreference(resources.getString(R.string.shared_preferences_settings_background_refresh_master))
        //master = findPreference("BackgroundRefreshMaster")
        //Registrieren des onPreferenceChangeListeners
        /*
        master.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
            preferenceScreen.removePreference(master)


            true
        }*/

        //Registrieren enes onPreferenceChangeListeners für das Deaktivieren von allen, sollte der Master ausgeschaltet werden
        val masterSwitch = findPreference(resources.getString(R.string.shared_preferences_settings_notifications_master))
        val vibrationSwitch = findPreference(resources.getString(R.string.shared_preferences_settings_notifications_vibration))
        val soundSwitch = findPreference(resources.getString(R.string.shared_preferences_settings_notification_sound))

        masterSwitch.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, any ->
            val pref : SwitchPreference = preference as SwitchPreference

            /*
            Achtung, das isChecked bedeutet genau das Gegenteil xD
             */
            if(pref.isChecked) {
                vibrationSwitch.isEnabled = false
                soundSwitch.isEnabled = false
            } else {
                vibrationSwitch.isEnabled = true
                soundSwitch.isEnabled = true
            }


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
            //act.supportActionBar!!.setIcon(R.drawable.ic_baseline_notifications_24px)
            act.supportActionBar!!.title = "Benachrichtigungen"
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        //master = findPreference("BackgroundRefreshMaster") as SwitchPreference
        //master.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, any -> true }
        super.onActivityCreated(savedInstanceState)
    }


}
