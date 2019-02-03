package de.codecrops.vertretungsplangymwen.pushnotifications

import android.app.Notification
import android.content.Context
import android.support.graphics.drawable.R
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import de.codecrops.vertretungsplangymwen.data.VertretungData

/**
 * Diese Klasse bietet alle nötigen Methoden um dem Nutzer zu informieren
 * @param context Der Kontext der Android App
 * @property list Eine Liste der Vertretungen
 * @property notifyList Eine Liste der erstellten, aber nicht gezeigten Benachrichtigungen
 * @property DEFAULT_CHANNEL_ID Die ID des standart Benachrichtigunschannels der App
 * @property DEFAULT_GROUP_ID Die ID der Benachrichtigungsgruppe
 */

class AppNotificationManager(val context: Context) {
    private val list: ArrayList<VertretungData> = arrayListOf()
    private val notifyList: ArrayList<NotificationCompat.Builder> = arrayListOf()
    private lateinit var summaryNotification: Notification

    companion object {
        const val DEFAULT_CHANNEL_ID = "de.codecrops.vertretungsplangymwen.notification-channel.default"
        const val DEFAULT_GROUP_ID = "de.codecrops.vertretungsplangymwen.notification-group.default"
        const val SUMMARY_ID = 0
    }

    //Baut aus einem Vertretungsobjekt eine passende Benachrichtigung
    private fun build() {
        for (v in list) {
            notifyList.add(NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                    .setSmallIcon(de.codecrops.vertretungsplangymwen.R.mipmap.ic_launcher_round)
                    .setContentTitle("${v.stunde}. Stunde ${v.vertretung}")
                    .setContentText(v.toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setGroup(DEFAULT_GROUP_ID)
                    .setAutoCancel(true))
        }

        summaryNotification = NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                .setContentTitle("title")
                //set content text to support devices running API level < 24
                .setContentText("text")
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                //build summary info into InboxStyle template
                .setStyle(NotificationCompat.InboxStyle()
                        .addLine(list[0].toString())
                        .addLine(list[1].toString())
                        .setBigContentTitle("${notifyList.size}. Vertretungen")
                        .setSummaryText("VertretungsplanGymWen"))
                //specify which group this notification belongs to
                .setGroup(DEFAULT_GROUP_ID)
                //set this notification as the summary for the group
                .setGroupSummary(true)
                .build()
    }

    //zeigt die Benachrichtigungen aus "notifyList"
    fun show() {
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancelAll()
        build()
        var count = 1
        for (n in notifyList) {
            with(notificationManagerCompat) {
                notify(count, n.build())
            }
            count++
        }
        with(notificationManagerCompat) {
            notify(SUMMARY_ID, summaryNotification)
        }
    }

    /**
     * lässt ein Vertretungobjekt hinzufügen
     * @param vertretungData Vertretungsobjekt
     */
    fun add(vertretungData: VertretungData) {
        list.add(vertretungData)
    }
}