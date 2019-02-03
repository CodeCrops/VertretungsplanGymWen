package de.codecrops.vertretungsplangymwen.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import de.codecrops.vertretungsplangymwen.Utils
import de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import java.util.*


class BackgroundJob : JobService() {

    companion object {
        const val JOB_ID = 0
    }

    private var jobCancelled = false

    override fun onStartJob(params: JobParameters?): Boolean {
        doBackgroundWork(params!!)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        jobCancelled = true
        return true
    }

    private fun doBackgroundWork(params: JobParameters) {
        Thread {
            if(!jobCancelled) {
                var empty = true
                Utils.fillDatabase(this)
                val notificationManager = AppNotificationManager(this)
                for(p in DBManager.getAllPreferences(this)) {
                    val list = DBManager.getVertretungenByKlasse(this, p.course, Calendar.getInstance().time)
                    if(!list.isEmpty()) {
                        for(vertretungData in list) {
                            notificationManager.add(vertretungData)
                        }
                        empty = false
                    }
                }
                if(!empty)  notificationManager.show()

                //boolean true für rescedule
                jobFinished(params, false)
            }
        }.start()
    }
}