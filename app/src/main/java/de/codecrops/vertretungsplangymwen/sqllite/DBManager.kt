package de.codecrops.vertretungsplangymwen.sqllite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DBManager(context: Context) {

    var dbHelper : VertretungsplanDBHelper
    var db : SQLiteDatabase
    var vpCursor : Cursor

    init {
        dbHelper = VertretungsplanDBHelper(context)
        db = dbHelper.writableDatabase
        vpCursor = updateVertretungsplanCursor()
    }

    /**
     * @param klasse Die Klasse, welche die Vertretung betrifft (in Kursen ist dies die Kursnummer)
     * @param stunde Die Stunde, in welcher die Vertretung stattfindet
     * @param vertretung Die Lehrkraft, welche die Vertretung durchführt (evtl. auch hier "entfällt XYZ")
     * @param fach Das Fach, welches anstelle des ursprünglichen Fachs unterrichtet wird (nur nötig, wenn es entfällt)
     * @param raum Der Raum, in welchem anstelle des ursprünglichen Raums unterrichtet wird (nicht nötig, wenn es entfällt)
     * @param sonstiges Sonstige Informationen
     */

    fun addDataToVertretungsplanDB(klasse: String, stunde: Int, vertretung: String, fach: String?, raum: String?, sonstiges: String?) {
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
     * @return Gibt den neuen Cursor des Vertretungsplans zurück : Cursor
     */

    fun updateVertretungsplanCursor():Cursor {
        /*
        Gibt den neuen Cursor zurück, aktualisiert den Cursor allerdings auch selbstständig -> ein zuweisen des rückgabewertes ist nicht notwendig
         */

        //Erhalte den Cursor von der DB
        val cursor = db.query(DBContracts.PlanContract.TABLE_NAME, null, null, null, null, null, null)

        //Schließe den alten Cursor um MemoryLeaks zu vermeiden
        vpCursor.close()

        //Aktualisiere den momentan gesetzten Cursor im DBManager
        vpCursor = cursor

        //Gebe den neuen Cursor zurück (nur beim ersten Mal benötigt, damit vpCursor einen Startwert hat
        return cursor
    }



}