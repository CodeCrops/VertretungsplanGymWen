package de.codecrops.vertretungsplangymwen.service

import android.app.job.JobParameters
import android.app.job.JobService

/**
 * @author K1TR1K
 */

class BackgroundJob : JobService() {

    companion object {
        const val JOB_ID = 0
    }

    private var jobChancelled = false

    override fun onStartJob(params: JobParameters?): Boolean {
        doBackgroundWork(params!!)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        jobChancelled = true
        return false
    }

    private fun doBackgroundWork(params: JobParameters) {
        Thread {
            fun run() {
                if(jobChancelled) {
                    return
                }
                //boolean true für rescedule
                jobFinished(params, false)
            }
        }.start()
    }
}