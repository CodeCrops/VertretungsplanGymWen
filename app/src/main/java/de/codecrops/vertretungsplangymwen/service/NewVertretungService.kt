package de.codecrops.vertretungsplangymwen.service

import android.app.job.JobParameters
import android.app.job.JobService
import de.codecrops.vertretungsplangymwen.Utils
import de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager


class NewVertretungService : JobService() {

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
                val list = Utils.checkForNewFilteredVertretung(this)
                val notifyManager = AppNotificationManager(this)
                notifyManager.isNew = true
                if(!list.isEmpty()) {
                    notifyManager.set(list.size)
                    notifyManager.show()
                }
                //boolean true für rescedule
                jobFinished(params, false)
            }
        }.start()
    }
}