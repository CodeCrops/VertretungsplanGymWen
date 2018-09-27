package de.codecrops.vertretungsplangymwen.sqllite

import android.provider.BaseColumns

object DBContracts {

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
        }
    }

}