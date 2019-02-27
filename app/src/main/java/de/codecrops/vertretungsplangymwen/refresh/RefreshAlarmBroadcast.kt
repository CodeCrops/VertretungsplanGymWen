package de.codecrops.vertretungsplangymwen.refresh

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.util.Log
import de.codecrops.vertretungsplangymwen.Utils
import de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager
import de.codecrops.vertretungsplangymwen.service.NewVertretungService
import java.lang.Exception

private val LOG_TAG = "RefreshAlarmBr"

class RefreshAlarmBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(LOG_TAG, "Alarm received!")

        Thread {
            Log.i(LOG_TAG, "Starting Alarm-Thread..")
            val list = Utils.checkForNewFilteredVertretung(context)
            val notifyManager = AppNotificationManager(context)
            notifyManager.isNew = true
            if(!list.isEmpty()) {
                notifyManager.setVertretungCount(list.size)
                notifyManager.show()
            }
        }.start()

        Log.i(LOG_TAG, "Alarm done!")
    }

}