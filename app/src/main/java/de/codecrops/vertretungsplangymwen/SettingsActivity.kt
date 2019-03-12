package de.codecrops.vertretungsplangymwen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log
import de.codecrops.vertretungsplangymwen.gui.customFragments.SettingsHeadersFragment
import de.codecrops.vertretungsplangymwen.refresh.RefreshManager
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    val LOG_TAG = "SettingsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, SettingsHeadersFragment())
                .commit()

        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle("Einstellungen")
        }

        toolbar.setNavigationOnClickListener { super.onBackPressed() }
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, (Class.forName(pref.fragment).newInstance() as Fragment))
                .addToBackStack(null)
                .commit()

        /* Ausgelagert in Fragments onStart() und onPause()
        when(pref.fragment) {
            "de.codecrops.vertretungsplangymwen.gui.customFragments.SettingsRefreshFragment" -> supportActionBar!!.title = "Hintergrundaktualisierung"
            "de.codecrops.vertretungsplangymwen.gui.customFragments.SettingsNotificationFragment" -> supportActionBar!!.title = "Benachrichtigungen"
            "de.codecrops.vertretungsplangymwen.gui.customFragments.SettingsHeadersFragment" -> supportActionBar!!.title = "Einstellungen"
        }
        */

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        supportActionBar!!.setTitle("Einstellungen")
        super.onBackPressed()
    }

    override fun onStop() {
        Log.i(LOG_TAG, "Restarting RefreshManager...")
        RefreshManager(this)
        super.onStop()
    }
}
