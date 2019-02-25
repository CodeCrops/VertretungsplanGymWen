package de.codecrops.vertretungsplangymwen.service

import android.app.job.JobParameters
import android.app.job.JobService
import de.codecrops.vertretungsplangymwen.Utils
import de.codecrops.vertretungsplangymwen.pushnotifications.AppNotificationManager
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import java.util.*

class AllVertretungService : JobService() {

    companion object {
        const val JOB_ID = 1
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
                /* So ist es gefiltert, aber ungefiltert zu testzwecken
                val list: ArrayList<VertretungData> = arrayListOf()
                for(p in DBManager.getAllPreferences(this)) {
                    list.addAll(DBManager.getVertretungenByKlasse(this, p.course, Calendar.getInstance().time))
                }
                */
                val list = DBManager.getAllVertretungen(this, Calendar.getInstance().time)
                val notifyManager = AppNotificationManager(this)
                notifyManager.isNew = false
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