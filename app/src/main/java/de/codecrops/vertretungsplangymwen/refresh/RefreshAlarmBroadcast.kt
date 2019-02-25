package de.codecrops.vertretungsplangymwen.refresh

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import de.codecrops.vertretungsplangymwen.Utils
import java.lang.Exception

private val LOG_TAG = "RefreshAlarmBr"

class RefreshAlarmBroadcast : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(LOG_TAG, "Alarm received!")
        try {
            Utils.fillDatabase(context)
            Log.i(LOG_TAG, "DB-fill request sent! Alarm done!")
        } catch (e: Exception) {
            Log.e(LOG_TAG, "DB-fill request failed! Alarm done!")
        }


    }

}