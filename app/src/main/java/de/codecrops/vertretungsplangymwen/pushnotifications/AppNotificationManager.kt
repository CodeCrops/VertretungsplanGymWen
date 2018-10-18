package de.codecrops.vertretungsplangymwen.pushnotifications

import android.content.Context
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

    companion object {
        const val DEFAULT_CHANNEL_ID = "default"
        const val DEFAULT_GROUP_ID = "default"
    }

    //Baut aus einem Vertretungsobjekt eine passende Benachrichtigung
    fun build() {
        for (v in list) {
            notifyList.add(NotificationCompat.Builder(context, DEFAULT_CHANNEL_ID)
                    //.setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle("${v.stunde} ${v.vertretung}")
                    .setContentText(v.toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setGroup(DEFAULT_GROUP_ID)
                    .setAutoCancel(true))
        }
    }

    //zeigt die Benachrichtigungen aus "notifyList"
    fun show() {
        var count = 0
        for (n in notifyList) {
            with(NotificationManagerCompat.from(context)) {
                notify(count, n.build())
            }
            count++
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