package de.codecrops.vertretungsplangymwen.settings

import android.content.Context
import android.support.v7.preference.PreferenceDataStore
import android.util.Log
import de.codecrops.vertretungsplangymwen.R

/*
Klasse, welche im PreferenceManager als CustomDataStore benutzt wird
 */

class SettingsSPAdapter(context: Context) : PreferenceDataStore() {

    private val LOG_TAG = this.javaClass.simpleName

    val sm = SettingsManager
    val sp = context.getSharedPreferences(context.resources.getString(R.string.shared_preferences_settings_path), Context.MODE_PRIVATE)

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        val ret = sp.getBoolean(key, defValue)
        Log.i(LOG_TAG, "Boolean-Werteabfrage für '$key' ausgeführt! Ergebnis: $ret")
        return ret
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        val ret = sp.getFloat(key, defValue)
        Log.i(LOG_TAG, "Float-Werteabfrage für '$key' ausgeführt! Ergebnis: $ret")
        return ret
    }

    override fun getInt(key: String?, defValue: Int): Int {
        val ret = sp.getInt(key, defValue)
        Log.i(LOG_TAG, "Int-Werteabfrage für '$key' ausgeführt! Ergebnis: $ret")
        return ret
    }

    override fun getLong(key: String?, defValue: Long): Long {
        val ret = sp.getLong(key, defValue)
        Log.i(LOG_TAG, "Long-Werteabfrage für '$key' ausgeführt! Ergebnis: $ret")
        return ret
    }

    override fun getString(key: String?, defValue: String?): String? {
        val ret = sp.getString(key, defValue)
        Log.i(LOG_TAG, "String-Werteabfrage für '$key' ausgeführt! Ergebnis: $ret")
        return ret
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        val ret = sp.getStringSet(key, defValues)
        Log.i(LOG_TAG, "StringSet-Werteabfrage für '$key' ausgeführt! Ergebnis: $ret")
        return ret
    }

    override fun putBoolean(key: String?, value: Boolean) {
        Log.i(LOG_TAG, "Boolean wird in SettingsSP eingefügt... (key: '$key', value: '$value')")
        with(sp.edit()) {
            putBoolean(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key', value: '$value')")
    }

    override fun putFloat(key: String?, value: Float) {
        Log.i(LOG_TAG, "Float wird in SettingsSP eingefügt... (key: '$key', value: '$value')")
        with(sp.edit()) {
            putFloat(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key', value: '$value')")
    }

    override fun putInt(key: String?, value: Int) {
        Log.i(LOG_TAG, "Int wird in SettingsSP eingefügt... (key: '$key', value: '$value')")
        with(sp.edit()) {
            putInt(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key', value: '$value')")
    }

    override fun putLong(key: String?, value: Long) {
        Log.i(LOG_TAG, "Long wird in SettingsSP eingefügt... (key: '$key', value: '$value')")
        with(sp.edit()) {
            putLong(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key', value: '$value')")
    }

    override fun putString(key: String?, value: String?) {
        Log.i(LOG_TAG, "String wird in SettingsSP eingefügt... (key: '$key', value: '$value')")
        with(sp.edit()) {
            putString(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key', value: '$value')")
    }

    override fun putStringSet(key: String?, values: MutableSet<String>?) {
        Log.i(LOG_TAG, "StringSet wird in SettingsSP eingefügt... (key: '$key')")
        with(sp.edit()) {
            putStringSet(key, values)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key')")
    }

}