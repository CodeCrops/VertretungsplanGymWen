package de.codecrops.vertretungsplangymwen.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DBManager(context: Context) {

    var dataBaseHelper : DataBaseHelper
    var db : SQLiteDatabase

    init {
        dataBaseHelper = DataBaseHelper(context)

        //onCreate() im Helper wird erst gecallt, wenn writeableDatabase oder readableDatabase aufgerufen wird -> "initAufrufen"

        //es ist (nach dem initialisieren) egal von welchem Objekt die DB geholt wird, da es sich um die gleiche DB handelt
        db = dataBaseHelper.writableDatabase
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
        val selection = "${DBContracts.PlanContract.COLUMN_KLASSE} = '$klasse'"

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
        if(cursor.moveToNext()) {
            cursor.close()
            return true
        } else cursor.close(); return false
    }

    /**
     * @param klasse Die Klasse/der Kurs, in welchem sich der Schüler befindet
     * @return ArrayList<ScheduleEntry> : Eine ArrayList, welche pro element eine Vertretungsstunde enthält
     */
    fun getVertretungenByKlasse(klasse: String): ArrayList<ScheduleEntry> {

        //result wird vorbereitet
        val result = ArrayList<ScheduleEntry>()

        //cursor wird von DB geholt
        val cursor = getPlanCursorByKlasse(klasse)

        //while schleife zum bearbeiten des Cursors
        while (cursor.moveToNext()) {
            result.add( //füget result die neue Vertretungsstunde hinzu
                    ScheduleEntry( //erstellt ein neues Objekt von ScheduleEntry
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

    /**
     * @param kuerzel Das Kürzel der Lehrkraft, welche überprüft wird
     * @return Boolean - true:ist bereits enthalten ; false:noch nicht enthalten
     */
    fun isLehrerAlreadyInDB(kuerzel: String) : Boolean {
        val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '$kuerzel'"
        val cursor = db.query(DBContracts.LehrerContract.TABLE_NAME, null, selection, null, null, null, null, null)
        if(cursor.moveToNext()) {
            cursor.close()
            return true
        } else cursor.close(); return false
    }


    /**
     * @param kuerzel Das Kürzel des Lehrers, welcher entfernt werden soll
     */
    fun removeLehrerFromDB(kuerzel: String) {
        val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '$kuerzel'"
        db.delete(DBContracts.LehrerContract.TABLE_NAME, selection, null)
    }

    /**
     * @param kuerzel Das Kürzel der Lehrkraft
     * @param nachname Der Nachname der Lehrkraft
     * @param vorname Der Vorname der Lehrkraft (nullable, falls nicht bekannt)
     * @param geschlecht Das Geschlecht der Lehrkraft
     */
    fun addLehrerToDB(kuerzel: String, nachname: String, vorname: String?, geschlecht: String) {

        //Lehrer wird entfernt, falls er bereits vorhanden ist -> Update wäre auch möglich, aber jetzt isses halt so ;)
        if(isLehrerAlreadyInDB(kuerzel)) removeLehrerFromDB(kuerzel)

        //Erstellt ein ContentValues-Objekt und fügt die Daten hinzu
        val values = ContentValues().apply {
            put(DBContracts.LehrerContract.COLUMN_KUERZEL, kuerzel)
            put(DBContracts.LehrerContract.COLUMN_NACHNAME, nachname)
            if(vorname != null) put(DBContracts.LehrerContract.COLUMN_VORNAME, vorname)
            put(DBContracts.LehrerContract.COLUMN_GESCHLECHT, geschlecht)
        }

        //Erstellt eine neue Zeile in der DB mit den Daten von values
        db.insert(DBContracts.LehrerContract.TABLE_NAME, null, values)
    }

    /**
     * @param kuerzel Das Kürzel der Lehrkraft von welcher man die Informationen haben möchte
     * @return LehrerData: ein Objekt gefüllt mit den Daten des spezifischen Lehrers
     */
    fun getLehrerInformation(kuerzel: String) : LehrerData {
        val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '$kuerzel'"
        val cursor = db.query(DBContracts.LehrerContract.TABLE_NAME, null, selection, null, null, null, null, null)
        if(cursor.moveToNext()) {
             val result = LehrerData(
                    kuerzel,
                    cursor.getString(cursor.getColumnIndex(DBContracts.LehrerContract.COLUMN_NACHNAME)),
                    cursor.getString(cursor.getColumnIndex(DBContracts.LehrerContract.COLUMN_VORNAME)),
                    cursor.getString(cursor.getColumnIndex(DBContracts.LehrerContract.COLUMN_GESCHLECHT))
             )
            cursor.close()
            return result
        } else cursor.close(); return LehrerData(kuerzel, "FEHLER", "FEHLER", "FEHLER")
    }

}