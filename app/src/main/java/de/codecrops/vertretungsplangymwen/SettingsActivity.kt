package de.codecrops.vertretungsplangymwen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.MenuItem
import android.view.View
import de.codecrops.vertretungsplangymwen.gui.customFragments.SettingsHeadersFragment
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

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
            //Sollte dafür sorgen, dass der ZurückButton in der Actionbar auch die onBackPressed() triggert... geht aber irgendwie nicht :(
            //TODO: Fix
            fun onSupportNavigateUp() : Boolean {
                onBackPressed()
                return true
            }
        }

        toolbar.setNavigationOnClickListener { super.onBackPressed() }
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, (Class.forName(pref.fragment).newInstance() as Fragment))
                .addToBackStack(null)
                .commit()

        when(pref.fragment) {
            "de.codecrops.vertretungsplangymwen.gui.customFragments.SettingsRefreshFragment" -> supportActionBar!!.title = "Hintergrundaktualisierung"
            "de.codecrops.vertretungsplangymwen.gui.customFragments.SettingsNotificationFragment" -> supportActionBar!!.title = "Benachrichtigungen"
        }


        return true
    }

    override fun onBackPressed() {
        supportActionBar!!.setTitle("Einstellungen")
        super.onBackPressed()
    }
}
