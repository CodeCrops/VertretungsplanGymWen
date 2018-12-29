package de.codecrops.vertretungsplangymwen.sqlite

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object DBContracts {

    const val CONTENT_AUTHORITY = "de.codecrops.vertretungsplangymwen"
    val BASE_CONTENT_URI = Uri.parse("content:// $CONTENT_AUTHORITY")
    const val PATH_SCHEDULE = PlanContract.TABLE_NAME
    const val PATH_LEHRER = LehrerContract.TABLE_NAME


    //Inner class implements BaseColums
    class PlanContract : BaseColumns {
        companion object {

            /*
            Das sind quasi die Schlüsselwörter, die man in einer SQL-Query angibt
            Sie werden hier nur festgelegt, damit man global darauf zugreifen kann
            und sie hier einfach ändern kann und sich nicht verschreibt.
             */

            /*
            ACHTUNG: Bei Änderungen hier, muss im DBHelper die Version erhöht werden
                     Dadurch wird die gesamte DB geflusht und für den neuen Contract aufgebaut.
             */

            const val TABLE_NAME = "vertretungsplan"
            const val _ID = BaseColumns._ID
            const val COLUMN_LISTID = "listID"
            const val COLUMN_KLASSE = "klasse"
            const val COLUMN_STUNDE = "stunde"
            const val COLUMN_VERTRETUNG = "vertretung"
            const val COLUMN_FACH = "fach"
            const val COLUMN_RAUM = "raum"
            const val COLUMN_SONSTIGES = "sonstiges"

            //"MIME"Types
            val CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE
            val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SCHEDULE

            val CONTENT_URI : Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SCHEDULE)

        }
    }

    //Inner class implements BaseColumns
    class LehrerContract : BaseColumns {
        companion object {

            /*
            ACHTUNG: Bei Änderungen hier, muss im DBHelper die Version erhöht werden
                     Dadurch wird die gesamte DB geflusht und für den neuen Contract aufgebaut.
             */

            const val TABLE_NAME = "lehrer"
            const val _ID = BaseColumns._ID
            const val COLUMN_LISTID = "listID"
            const val COLUMN_KUERZEL = "kuerzel"
            const val COLUMN_VORNAME = "vorname"
            const val COLUMN_NACHNAME = "nachname"
            const val COLUMN_GESCHLECHT = "geschlecht"

            //"MIME"Types
            val CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LEHRER
            val CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LEHRER

            val CONTENT_URI : Uri = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LEHRER)
        }
    }

}