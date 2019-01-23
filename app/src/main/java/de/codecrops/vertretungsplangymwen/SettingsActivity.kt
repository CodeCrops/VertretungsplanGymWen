package de.codecrops.vertretungsplangymwen

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.preference.PreferenceManager
import de.codecrops.vertretungsplangymwen.settings.SettingsManager
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting SP-Adapter as StandardValues for PreferenceManager
        SettingsManager.setNotificationMaster(applicationContext, false)
        PreferenceManager.setDefaultValues(applicationContext, SettingsManager.SHARED_PREFERENCES_SETTINGS_PATH, Context.MODE_PRIVATE, R.xml.settings_notifications, false)


        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle("Einstellungen")
        }

        toolbar.setNavigationOnClickListener { super.onBackPressed() }
    }
}
