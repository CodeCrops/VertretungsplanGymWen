package de.codecrops.vertretungsplangymwen.settings

import android.content.Context
import android.support.v7.preference.PreferenceDataStore
import android.util.Log

/*
Klasse, welche im PreferenceManager als CustomDataStore benutzt wird
 */

class SettingsSPAdapter(context: Context) : PreferenceDataStore() {

    private val LOG_TAG = this.javaClass.simpleName

    val sm = SettingsManager
    val sp = context.getSharedPreferences(sm.SHARED_PREFERENCES_SETTINGS_PATH, Context.MODE_PRIVATE)

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        Log.i(LOG_TAG, "Boolean-Werteabfrage für '$key' ausgeführt! Ergebnis: ${sp.getBoolean(key, defValue)}")
        return sp.getBoolean(key, defValue)
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        Log.i(LOG_TAG, "Float-Werteabfrage für '$key' ausgeführt! Ergebnis: ${sp.getFloat(key, defValue)}")
        return sp.getFloat(key, defValue)
    }

    override fun getInt(key: String?, defValue: Int): Int {
        Log.i(LOG_TAG, "Int-Werteabfrage für '$key' ausgeführt! Ergebnis: ${sp.getInt(key, defValue)}")
        return sp.getInt(key, defValue)
    }

    override fun getLong(key: String?, defValue: Long): Long {
        Log.i(LOG_TAG, "Long-Werteabfrage für '$key' ausgeführt! Ergebnis: ${sp.getLong(key, defValue)}")
        return sp.getLong(key, defValue)
    }

    override fun getString(key: String?, defValue: String?): String? {
        Log.i(LOG_TAG, "String-Werteabfrage für '$key' ausgeführt! Ergebnis: ${sp.getString(key, defValue)}")
        return sp.getString(key, defValue)
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String> {
        Log.i(LOG_TAG, "StringSet-Werteabfrage für '$key' ausgeführt! Ergebnis: ${sp.getStringSet(key, defValues)}")
        return sp.getStringSet(key, defValues)
    }

    override fun putBoolean(key: String?, value: Boolean) {
        Log.i(LOG_TAG, "Boolean wird in SettingsSP eingefügt... (key: '$key')")
        with(sp.edit()) {
            putBoolean(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key')")
    }

    override fun putFloat(key: String?, value: Float) {
        Log.i(LOG_TAG, "Float wird in SettingsSP eingefügt... (key: '$key')")
        with(sp.edit()) {
            putFloat(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key')")
    }

    override fun putInt(key: String?, value: Int) {
        Log.i(LOG_TAG, "Int wird in SettingsSP eingefügt... (key: '$key')")
        with(sp.edit()) {
            putInt(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key')")
    }

    override fun putLong(key: String?, value: Long) {
        Log.i(LOG_TAG, "Long wird in SettingsSP eingefügt... (key: '$key')")
        with(sp.edit()) {
            putLong(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key')")
    }

    override fun putString(key: String?, value: String?) {
        Log.i(LOG_TAG, "String wird in SettingsSP eingefügt... (key: '$key')")
        with(sp.edit()) {
            putString(key, value)
            apply()
        }
        Log.i(LOG_TAG, "Einfügen erfolgreich! (key: '$key')")
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