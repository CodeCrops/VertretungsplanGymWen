package de.codecrops.vertretungsplangymwen.settings

import android.content.Context
import de.codecrops.vertretungsplangymwen.R
import java.util.regex.Pattern

const val SHARED_PREFERENCES_SETTINGS_PATH = R.string.shared_preferences_settings_path

//Classes and Courses of the User (used in FAB..)
const val SHARED_PREFERENCES_SETTINGS_KLASSEN = R.string.shared_preferences_settings_klassen

//Background-Refresh-Settings
const val SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_MASTER = R.string.shared_preferences_settings_background_refresh_master
const val SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_SMART = R.string.shared_preferences_settings_background_refresh_smart
const val SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO = R.string.shared_preferences_settings_background_refresh_auto
const val SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO_INTERVAL = R.string.shared_preferences_settings_background_refresh_auto_interval
const val SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO_CLOCK = R.string.shared_preferences_settings_background_refresh_auto_clock

//Notification-Settings
const val SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_MASTER = R.string.shared_preferences_settings_notifications_master
const val SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_VIBRATION = R.string.shared_preferences_settings_notifications_vibration
const val SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_SOUND = R.string.shared_preferences_settings_notification_sound

class SettingsManager() {
    companion object {
        //Convert ID (int) von R.string to String
        private fun getIDString(context: Context, id: Int) : String {
            return context.resources.getString(id)
        }

        // >>>>>>>>>> KLASSEN-Settings <<<<<<<<<<

        /**
         * @param context Context of App
         * @return List<String> containing each Class (size=1) or Course (size>1) fo the user
         */
        fun getKlassen(context: Context) : List<String> {
            //Gets access to sharedPrefs
            val sharedPref = context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE)
            //Gets String of attended classes divided by ":" -> for example (Oberstufe): "1sk1:1g3:1m4:" , example (Unter-/Mittelstufe): "10b:"
            val saved = sharedPref.getString(getIDString(context,SHARED_PREFERENCES_SETTINGS_KLASSEN), "")
            return saved.split(Pattern.compile(":"), 0)
        }

        /**
         * @param context Context of App
         */
        fun addKlasse(context: Context, newClass: String) {
            val sharedPref = context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE)
            var saved = sharedPref.getString(getIDString(context,SHARED_PREFERENCES_SETTINGS_KLASSEN), "")
            with(sharedPref.edit()) {
                putString(getIDString(context,SHARED_PREFERENCES_SETTINGS_KLASSEN), "$saved$newClass:")
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearKlassen(context: Context) {
            val sharedPref = context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_KLASSEN))
                apply()
            }
        }


        // >>>>>>>>>> Background-Refresh-Master-Settings <<<<<<<<<<


        /**
         * @param context Context of App
         * @return Boolean Value indicating the Status of the BR-Master-Switch (false=off, true=on)
         */
        fun getBackgroundRefreshMaster(context: Context) : Boolean {
            return context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).getBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_MASTER), false)
        }

        /**
         * @param context Context of App
         * @param newStatus New Status of BR-Master-Switch (false=off, true=on)
         */
        fun setBackgroundRefreshMaster(context: Context, newStatus: Boolean) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                putBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_MASTER), newStatus)
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearBackgroundRefreshMaster(context: Context) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_MASTER))
                apply()
            }
        }


        // >>>>>>>>>> Background-Refresh-Smart-Settings <<<<<<<<<<


        /**
         * @param context Context of App
         * @return Boolean Value indicating the Status of the BR-Smart-Switch (false=off, true=on)
         */
        fun getBackgroundRefreshSmart(context: Context) : Boolean {
            return context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).getBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_SMART), false)
        }

        /**
         * @param context Context of App
         * @param newStatus New Status of BR-Smart-Switch (false=off, true=on)
         */
        fun setBackgroundRefreshSmart(context: Context, newStatus: Boolean) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                putBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_SMART), newStatus)
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearBackgroundRefreshSmart(context: Context) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_SMART))
                apply()
            }
        }


        // >>>>>>>>>> Background-Refresh-Auto-Settings <<<<<<<<<<


        /**
         * @param context Context of App
         * @return Boolean Value indicating the Status of the BR-Auto-Switch (false=off, true=on)
         */
        fun getBackgroundRefreshAuto(context: Context) : Boolean {
            return context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).getBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO), false)
        }

        /**
         * @param context Context of App
         * @param newStatus New Status of BR-Auto-Switch (false=off, true=on)
         */
        fun setBackgroundRefreshAuto(context: Context, newStatus: Boolean) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                putBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO), newStatus)
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearBackgroundRefreshAuto(context: Context) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO))
                apply()
            }
        }


        // >>>>>>>>>> Background-Refresh-Auto-Interval-Settings <<<<<<<<<<


        /**
         * @param context Context of App
         * @return Integer Value indicating the Value of the BR-Auto-Interval in Minutes
         */
        fun getBackgroundRefreshAutoInterval(context: Context) : Int {
            return context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).getInt(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO_INTERVAL), 1)
        }

        /**
         * @param context Context of App
         * @param newValue New Value of the BR-Auto-Interval in Minutes
         */
        fun setBackgroundRefreshAutoInterval(context: Context, newValue: Int) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                putInt(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO_INTERVAL), newValue)
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearBackgroundRefreshAutoInterval(context: Context) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO_INTERVAL))
                apply()
            }
        }


        // >>>>>>>>>> Background-Refresh-Auto-Clock-Settings <<<<<<<<<<


        /**
         * @param context Context of App
         * @return String Value indicating the Value of the BR-Auto-Clock in ClockFormat out of TimePicker.toString()
         */
        fun getBackgroundRefreshAutoClock(context: Context) : String {
            return context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).getString(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO_CLOCK), "")
        }

        /**
         * @param context Context of App
         * @param newValue New Value of the BR-Auto-Clock in String out of TimePicker.toString()
         */
        fun setBackgroundRefreshAutoClock(context: Context, newValue: String) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                putString(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO_CLOCK), newValue)
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearBackgroundRefreshAutoClock(context: Context) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_BACKGROUND_REFRESH_AUTO_CLOCK))
                apply()
            }
        }


        // >>>>>>>>>> Notification-Master-Settings <<<<<<<<<<


        /**
         * @param context Context of App
         * @return Boolean Value indicating the Status of the Nofification-Master-Switch (false=off, true=on)
         */
        fun getNotifificationMaster(context: Context) : Boolean {
            return context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).getBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_MASTER), false)
        }

        /**
         * @param context Context of App
         * @param newStatus New Status of Notification-Master-Switch (false=off, true=on)
         */
        fun setNotificationMaster(context: Context, newStatus: Boolean) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                putBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_MASTER), newStatus)
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearNotificationMaster(context: Context) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_MASTER))
                apply()
            }
        }


        // >>>>>>>>>> Notification-Vibration-Settings <<<<<<<<<<


        /**
         * @param context Context of App
         * @return Boolean Value indicating the Status of the Nofification-Vibration-Switch (false=off, true=on)
         */
        fun getNotifificationVibration(context: Context) : Boolean {
            return context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).getBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_VIBRATION), false)
        }

        /**
         * @param context Context of App
         * @param newStatus New Status of Notification-Vibration-Switch (false=off, true=on)
         */
        fun setNotificationVibration(context: Context, newStatus: Boolean) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                putBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_VIBRATION), newStatus)
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearNotificationVibration(context: Context) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_VIBRATION))
                apply()
            }
        }


        // >>>>>>>>>> Notification-Master-Settings <<<<<<<<<<


        /**
         * @param context Context of App
         * @return Boolean Value indicating the Status of the Nofification-Sound-Switch (false=off, true=on)
         */
        fun getNotifificationSound(context: Context) : Boolean {
            return context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).getBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_SOUND), false)
        }

        /**
         * @param context Context of App
         * @param newStatus New Status of Notification-Sound-Switch (false=off, true=on)
         */
        fun setNotificationSound(context: Context, newStatus: Boolean) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                putBoolean(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_SOUND), newStatus)
                apply()
            }
        }

        /**
         * @param context Context of App
         */
        fun clearNotificationSound(context: Context) {
            with(context.getSharedPreferences(getIDString(context,SHARED_PREFERENCES_SETTINGS_PATH), Context.MODE_PRIVATE).edit()) {
                remove(getIDString(context,SHARED_PREFERENCES_SETTINGS_NOTIFICATIONS_SOUND))
                apply()
            }
        }
    }
}