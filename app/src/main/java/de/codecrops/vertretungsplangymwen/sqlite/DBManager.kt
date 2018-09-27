package de.codecrops.vertretungsplangymwen.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DBManager(context: Context) {

    var dbHelper : VertretungsplanDBHelper
    var db : SQLiteDatabase

    init {
        dbHelper = VertretungsplanDBHelper(context)
        db = dbHelper.writableDatabase
    }

    /**
     * @param klasse Die Klasse, welche die Vertretung betrifft (in Kursen ist dies die Kursnummer)
     * @param stunde Die Stunde, in welcher die Vertretung stattfindet
     * @param vertretung Die Lehrkraft, welche die Vertretung durchführt (evtl. auch hier "entfällt XYZ")
     * @param fach Das Fach, welches anstelle des ursprünglichen Fachs unterrichtet wird (nur nötig, wenn es entfällt)
     * @param raum Der Raum, in welchem anstelle des ursprünglichen Raums unterrichtet wird (nicht nötig, wenn es entfällt)
     * @param sonstiges Sonstige Informationen
     */

    fun addDataToVertretungsDB(klasse: String, stunde: Int, vertretung: String, fach: String?, raum: String?, sonstiges: String?) {
        /*
        ACHTUNG:

            - Die Parameter klasse, stunde und vertretung sind nicht nullable -> sie müssen übergeben werden!
            - Die Paramter fach, raum und sonstiges sind nullable -> sie können weggelassen werden und sollten dies auch bei leeren Werten bitte werden! Kein "" anstatt null
         */

        //Erstellt ein ContentValues-Objekt und fügt die Daten hinzu
        val values = ContentValues().apply {
            put(DBContracts.PlanContract.COLUMN_KLASSE, klasse)
            put(DBContracts.PlanContract.COLUMN_STUNDE, stunde)
            put(DBContracts.PlanContract.COLUMN_VERTRETUNG, vertretung)
            if(fach != null) put(DBContracts.PlanContract.COLUMN_FACH, fach)
            if(raum != null) put(DBContracts.PlanContract.COLUMN_RAUM, raum)
            if(sonstiges != null) put(DBContracts.PlanContract.COLUMN_SONSTIGES, sonstiges)
        }

        //Erstellt eine neue Zeile in der DB mit den Daten von values
        db.insert(DBContracts.PlanContract.TABLE_NAME, null, values)
    }

    /**
     * @return Cursor
     */
    fun getPlanCursorByKlasse(klasse: String): Cursor {

        //Erstellt die Selection (die WHERE-Clause)
        val selection = "${DBContracts.PlanContract.COLUMN_KLASSE} = $klasse"

        //Erhalte den Cursor von der DB
        val cursor = db.query(DBContracts.PlanContract.TABLE_NAME, null, selection, null, null, null, null)

        //Gebe den Cursor zurück
        return cursor
    }

    /**
     * @param klasse Die Klasse/der Kurs, in welchem sich der Schüler befindet
     * @return Boolean, True : "Schüler hat Vertretung", False : "Schüler hat normal Unterricht"
     */
    fun hasPupilVertretung(klasse: String) : Boolean {
        val cursor = getPlanCursorByKlasse(klasse)
        if(cursor.moveToNext()) return true
        return false
    }

    /**
     * @param klasse Die Klasse/der Kurs, in welchem sich der Schüler befindet
     * @return ArrayList<VertretungsData> : Eine ArrayList, welche pro element eine Vertretungsstunde enthält
     */
    fun getVertretungenByKlasse(klasse: String): ArrayList<VertretungsData> {

        //result wird vorbereitet
        val result = ArrayList<VertretungsData>()

        //cursor wird von DB geholt
        val cursor = getPlanCursorByKlasse(klasse)

        //while schleife zum bearbeiten des Cursors
        while (cursor.moveToNext()) {
            result.add( //füget result die neue Vertretungsstunde hinzu
                    VertretungsData( //erstellt ein neues Objekt von VertretungsData
                            cursor.getString(cursor.getColumnIndex(DBContracts.PlanContract.COLUMN_KLASSE)), //Klasse
                            cursor.getInt(cursor.getColumnIndex(DBContracts.PlanContract.COLUMN_STUNDE)), //Stunde
                            cursor.getString(cursor.getColumnIndex(DBContracts.PlanContract.COLUMN_VERTRETUNG)), //Vertretung
                            cursor.getString(cursor.getColumnIndex(DBContracts.PlanContract.COLUMN_FACH)), //Fach
                            cursor.getString(cursor.getColumnIndex(DBContracts.PlanContract.COLUMN_RAUM)), //Raum
                            cursor.getString(cursor.getColumnIndex(DBContracts.PlanContract.COLUMN_SONSTIGES))) //Sonstiges
            )
        }

        //Schließt den Cursor, damit keine MemoryLeaks auftreten
        cursor.close()

        return result
    }

}