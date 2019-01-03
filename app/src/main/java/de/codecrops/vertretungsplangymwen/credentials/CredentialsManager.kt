package de.codecrops.vertretungsplangymwen.credentials

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64


const val SHARED_PREFERENCES_PATH = "de.codecrops.vertretungsplangymwen.HTTPCredentials"
const val SHARED_PREFERENCES_USERNAME_KEY = "HTTPUser"
const val SHARED_PREFERENCES_PASSWORD_KEY = "HTTPPass"

class CredentialsManager {
    companion object {

        /**
         * @param context Context of the Application - needed to get access to SharedPreferences
         */
        fun setHTTPCredentials(context: Context, username: String, password: String) {
            val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_PATH, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(SHARED_PREFERENCES_USERNAME_KEY, username)
                putString(SHARED_PREFERENCES_PASSWORD_KEY, password)
                apply()
            }
        }

        /**
         * @param context Context of the Application - needed to get access to SharedPreferences
         * @return Username saved for the HTTPLogin
         */
        fun getHTTPUsername(context: Context) : String {
            val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_PATH, Context.MODE_PRIVATE)
            val user = sharedPref.getString(SHARED_PREFERENCES_USERNAME_KEY, "anonymous")
            return user
        }

        /**
         * @param context Context of the Application - needed to get access to SharedPreferences
         * @return Password saved for the HTTPLogin
         */
        fun getHTTPPassword(context: Context) : String {
            val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_PATH, Context.MODE_PRIVATE)
            val pass = sharedPref.getString(SHARED_PREFERENCES_PASSWORD_KEY, "")
            return pass
        }

        /**
         * @param context Context of the Application - needed to get access to SharedPreferences
         */
        fun deleteHTTPCredentials(context: Context)  {
            val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_PATH, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove(SHARED_PREFERENCES_USERNAME_KEY)
                remove(SHARED_PREFERENCES_PASSWORD_KEY)
                apply()
            }
        }

        /**
         * @param context Context of the Application - needed to get access to SharedPreferences
         * @return Base64 encoded String - ready for Website-Login
         */

        fun convertToBase64(context: Context) : String {
            val sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_PATH, Context.MODE_PRIVATE)
            var key = (sharedPref.getString(SHARED_PREFERENCES_USERNAME_KEY, "") + ":" + sharedPref.getString(SHARED_PREFERENCES_PASSWORD_KEY, ""))
            return Base64.encodeToString(key.toByteArray(), 0).replace("\n", "")
        }

    }

}