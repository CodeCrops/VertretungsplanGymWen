package de.codecrops.vertretungsplangymwen.service

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import de.codecrops.vertretungsplangymwen.Utils


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
                Utils.fillDatabase(this)
                //boolean true für rescedule
                jobFinished(params, false)
            }
        }.start()
    }
}