package de.codecrops.vertretungsplangymwen.pushnotifications

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import de.codecrops.vertretungsplangymwen.LoginActivity
import de.codecrops.vertretungsplangymwen.R

/**
 * Diese Klasse bietet alle nötigen Methoden um dem Nutzer zu informieren
 * @param context Der Kontext der Android App
 * @property list Eine Liste der Vertretungen
 * @property notifyList Eine Liste der erstellten, aber nicht gezeigten Benachrichtigungen
 * @property DEFAULT_CHANNEL_ID Die ID des standart Benachrichtigunschannels der App
 * @property DEFAULT_GROUP_ID Die ID der Benachrichtigungsgruppe
 */

class AppNotificationManager(val context: Context) {
    var isNew = false

    private var vertretungCount = 0
    private lateinit var notification : Notification

    companion object {
        const val DEFAULT_CHANNEL_ID = "de.codecrops.vertretungsplangymwen.notification-channel.default"
        const val DEFAULT_GROUP_ID = "de.codecrops.vertretungsplangymwen.notification-group.default"
        const val DEFAULT_NOTIFICATION_ID = 1
    }

    //Baut aus einem Vertretungsobjekt eine passende Benachrichtigung
    private fun build() {
        val notificationIntent = Intent(context, LoginActivity::class.java)
        notificationIntent.action = Intent.ACTION_MAIN
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val notificationBuilder = NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                .setSmallIcon(de.codecrops.vertretungsplangymwen.R.mipmap.ic_launcher_round)
                .setContentTitle("$vertretungCount Vertretungen!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        if(isNew) {
            notificationBuilder.setContentText("Du hast $vertretungCount neue Vertretungen!")
        } else {
            notificationBuilder.setContentText("Du hast $vertretungCount Vertretungen!")
        }
        notification = notificationBuilder.build()
    }

    //zeigt die Benachrichtigungen aus "notifyList"
    fun show() {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        build()
        with(notificationManagerCompat) {
            notify(DEFAULT_NOTIFICATION_ID, notification)
        }
    }

    fun cancel() {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancelAll()
    }

    /**
     * lässt ein Vertretungobjekt hinzufügen
     * @param vertretungData Vertretungsobjekt
     */
    fun setVertretungCount(count: Int) {
        vertretungCount = count
    }
}