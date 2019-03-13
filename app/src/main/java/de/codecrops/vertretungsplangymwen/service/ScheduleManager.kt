package de.codecrops.vertretungsplangymwen.service

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

object ScheduleManager {
    fun scheduleNewVertretungJob(context: Context, minuteGap: Long) {
        val componentName = ComponentName(context, NewVertretungService::class.java)
        val jobInfo = JobInfo.Builder(NewVertretungService.JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(minuteGap * 60 * 1000)
                .build()

        val scheduler: JobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(jobInfo)
        if(resultCode == JobScheduler.RESULT_SUCCESS) {
        }
    }

    fun scheduleAllVertretungJob(context: Context, minuteGap: Long) {
        val componentName = ComponentName(context, AllVertretungService::class.java)
        val jobInfo = JobInfo.Builder(AllVertretungService.JOB_ID, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setPeriodic(minuteGap * 60 * 1000)
                .build()

        val scheduler: JobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(jobInfo)
        if(resultCode == JobScheduler.RESULT_SUCCESS) {
        }
    }

    fun chancelNewVertretungJob(context: Context) {
        val scheduler: JobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(NewVertretungService.JOB_ID)
    }

    fun chancelAllVertretungJob(context: Context) {
        val scheduler: JobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(AllVertretungService.JOB_ID)
    }
}