package de.codecrops.vertretungsplangymwen.sqllite

import android.provider.BaseColumns

object DBContract {

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


            val TABLE_NAME = "vertretungsplan"
            val _ID = BaseColumns._ID
            val COLUMN_LISTID = "listID"
            val COLUMN_KLASSE = "klasse"
            val COLUMN_STUNDE = "stunde"
            val COLUMN_VERTRETUNG = "vertretung"
            val COLUMN_FACH = "fach"
            val COLUMN_RAUM = "raum"
            val COLUMN_SONSTIGES = "sonstiges"
        }
    }

}