package de.codecrops.vertretungsplangymwen.refresh

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.service.ScheduleManager
import de.codecrops.vertretungsplangymwen.settings.SettingsManager
import java.lang.Exception
import java.util.*

private val LOG_TAG = "RefreshManager"

class RefreshManager(val context: Context) {

    //Only gets initialized when the whole object is created (way of (re-)loading this manager)
    init {
        //ClockRefresher.cancelRefreshJobs(context)
        ClockRefresher.startRefreshJobs(context)
        //ScheduleRefresher.cancelRefreshJob(context)
        ScheduleRefresher.startRefreshJobs(context)
    }

    fun startAllJobs() {
        ClockRefresher.startRefreshJobs(context)
        ScheduleRefresher.startRefreshJobs(context)
    }

    fun cancelAllJobs() {
        ClockRefresher.cancelRefreshJobs(context)
        ScheduleRefresher.cancelRefreshJob(context)
    }

    class ClockRefresher {
        companion object {
            fun startRefreshJobs(context: Context) {

                //Refresh disabled by User
                if(!SettingsManager.getBackgroundRefreshMaster(context)) {
                    Log.i(LOG_TAG, "ClockRefresh cancel because BRMaster is disabled")
                    return
                }

                val clockstring = SettingsManager.getBackgroundRefreshAutoClock(context)
                val splittedStrings : List<String> = clockstring.split("//") //contains the time formated as "14:02"

                for(item in splittedStrings) {
                    if(splittedStrings.isNullOrEmpty() || clockstring.isNullOrBlank()) {
                        Log.e(LOG_TAG, "NO RefreshJobs (Clock) were started, because no RefreshAutoClocks were found in Settings")
                        return
                    } else {
                        val splittedTime : List<String> = item.split(":") //contains hour in [0] and minute in [1]
                        startRefreshJob(context, splittedTime[0].toInt(), splittedTime[1].toInt())
                    }

                }
                Log.i(LOG_TAG, "loadDatesOffDB done with ${splittedStrings.size} Dates loaded!")
            }

            fun startRefreshJob(context: Context, hour: Int, minute: Int) {
                //Getting AlarmManager from context
                val am : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                //Calendar einfügen, um den Startzeitpunkt in millis zu erhalten
                val cal : Calendar = Calendar.getInstance()
                cal.set(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH),
                        hour,
                        minute
                )

                //Creating Refresh-Intent (linking to RefreshAlarmBroadcast to catch the request)
                val intent = Intent(context, RefreshAlarmBroadcast::class.java)
                val penIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

                Log.i(LOG_TAG, "Scheduling new Repeating Refresh Job... ($hour:$minute)")

                am.setRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, AlarmManager.INTERVAL_DAY, penIntent)

                Log.i(LOG_TAG, "Schedule done!")
            }

            fun cancelRefreshJobs(context: Context) : Boolean {
                //Getting AlarmManager from context
                val am : AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                //Creating Refresh-Intent (linking to RefreshAlarmBroadcast to match the request)
                val intent = Intent(context, RefreshAlarmBroadcast::class.java)
                val penIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

                //Actually canceling the alarm
                try {
                    am.cancel(penIntent)
                    Log.i(LOG_TAG, "Cancelation of ClockRefreshAlarms was successful!")
                    return true
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Cancelation of ClockRefreshAlarms failed!")
                    return false
                }
            }
        }
    }

    class ScheduleRefresher {
        companion object {

            fun startRefreshJobs(context: Context) {

                //Refresh disabled by User
                if(!SettingsManager.getBackgroundRefreshMaster(context)) {
                    Log.i(LOG_TAG, "ScheduleRefresh canceled because BRMaster is disabled")
                    return
                }

                var interval = SettingsManager.getBackgroundRefreshAutoInterval(context).toLong()

                if(interval == 0.toLong()) {
                    Log.e(LOG_TAG, "NO RefreshJobs (Schedule) were started, because Interval from Settings was 0")
                } else {
                    Log.i(LOG_TAG, "Trying to start ScheduleRefreshJob for Interval $interval")
                    startRefreshJob(context, interval)
                }

            }

            fun startRefreshJob(context: Context, interval: Long) {
                Log.i(LOG_TAG, "Scheduling new Interval Refresh Job... ($interval min)")

                ScheduleManager.scheduleNewVertretungJob(context, interval)

                Log.i(LOG_TAG, "Schedule done!")
            }

            fun cancelRefreshJob(context: Context) : Boolean {
                ScheduleManager.chancelAllVertretungJob(context)
                Log.i(LOG_TAG, "Cancelation of ScheduleRefreshAlarms done!")
                return true
            }
        }
    }
}