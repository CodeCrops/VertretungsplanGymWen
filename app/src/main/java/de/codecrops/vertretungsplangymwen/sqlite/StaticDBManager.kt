package de.codecrops.vertretungsplangymwen.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class StaticDBManager {

    companion object {

        /**
         * @param context Context der App - muss übergeben werden, um DB-Connection aufzubauen
         * @param klasse Die Klasse, welche die Vertretung betrifft (in Kursen ist dies die Kursnummer)
         * @param stunde Die Stunde, in welcher die Vertretung stattfindet
         * @param vertretung Die Lehrkraft, welche die Vertretung durchführt (evtl. auch hier "entfällt XYZ")
         * @param fach Das Fach, welches anstelle des ursprünglichen Fachs unterrichtet wird (nur nötig, wenn es entfällt)
         * @param raum Der Raum, in welchem anstelle des ursprünglichen Raums unterrichtet wird (nicht nötig, wenn es entfällt)
         * @param sonstiges Sonstige Informationen
         */
        fun addDataToVertretungsDB(context: Context, klasse: String, stunde: Int, vertretung: String, fach: String?, raum: String?, sonstiges: String?) {

            val db = DataBaseHelper(context).writableDatabase

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

            //Schließt die DB-Connection
            db.close()
        }

        /**
         * @param context Context der App - muss übergeben werden, um DB-Connection aufzubauen
         * @return Cursor
         */
        fun getPlanCursorByKlasse(context: Context, klasse: String): Cursor {

            val db = DataBaseHelper(context).writableDatabase

            //Erstellt die Selection (die WHERE-Clause)
            val selection = "${DBContracts.PlanContract.COLUMN_KLASSE} = '$klasse'"

            //Erhalte den Cursor von der DB
            val cursor = db.query(DBContracts.PlanContract.TABLE_NAME, null, selection, null, null, null, null)

            //Schließt die DB-Connection
            db.close()

            //Gebe den Cursor zurück
            return cursor
        }

        /**
         * @param context Context der App - muss übergeben werden, um DB-Connection aufzubauen
         * @param klasse Die Klasse/der Kurs, in welchem sich der Schüler befindet
         * @return Boolean, True : "Schüler hat Vertretung", False : "Schüler hat normal Unterricht"
         */
        fun hasPupilVertretung(context: Context, klasse: String) : Boolean {
            val cursor = getPlanCursorByKlasse(context, klasse)
            if(cursor.moveToNext()) {
                cursor.close()
                return true
            } else cursor.close(); return false
        }

        /**
         * @param context Context der App - muss übergeben werden, um DB-Connection aufzubauen
         * @param klasse Die Klasse/der Kurs, in welchem sich der Schüler befindet
         * @return ArrayList<ScheduleEntry> : Eine ArrayList, welche pro element eine Vertretungsstunde enthält
         */
        fun getVertretungenByKlasse(context: Context, klasse: String): ArrayList<ScheduleEntry> {

            //result wird vorbereitet
            val result = ArrayList<ScheduleEntry>()

            //cursor wird von DB geholt
            val cursor = getPlanCursorByKlasse(context, klasse)

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
         * @param context Context der App - muss übergeben werden, um DB-Connection aufzubauen
         * @param kuerzel Das Kürzel der Lehrkraft, welche überprüft wird
         * @return Boolean - true:ist bereits enthalten ; false:noch nicht enthalten
         */
        fun isLehrerAlreadyInDB(context: Context, kuerzel: String) : Boolean {

            val db = DataBaseHelper(context).writableDatabase

            val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '$kuerzel'"
            val cursor = db.query(DBContracts.LehrerContract.TABLE_NAME, null, selection, null, null, null, null, null)
            if(cursor.moveToNext()) {
                cursor.close()
                db.close()
                return true
            } else cursor.close(); db.close(); return false
        }

        /**
         * @param context Context der App - muss übergeben werden, um DB-Connection aufzubauen
         * @param kuerzel Das Kürzel des Lehrers, welcher entfernt werden soll
         */
        fun removeLehrerFromDB(context: Context, kuerzel: String) {

            val db = DataBaseHelper(context).writableDatabase

            val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '$kuerzel'"
            db.delete(DBContracts.LehrerContract.TABLE_NAME, selection, null)

            db.close()
        }

        /**
         * @param context Context der App - muss übergeben werden, um DB-Connection aufzubauen
         * @param kuerzel Das Kürzel der Lehrkraft
         * @param nachname Der Nachname der Lehrkraft
         * @param vorname Der Vorname der Lehrkraft (nullable, falls nicht bekannt)
         * @param geschlecht Das Geschlecht der Lehrkraft
         */
        fun addLehrerToDB(context: Context, kuerzel: String, nachname: String, vorname: String?, geschlecht: String) {

            val db = DataBaseHelper(context).writableDatabase

            //Lehrer wird entfernt, falls er bereits vorhanden ist -> Update wäre auch möglich, aber jetzt isses halt so ;)
            if(isLehrerAlreadyInDB(context, kuerzel)) removeLehrerFromDB(context, kuerzel)

            //Erstellt ein ContentValues-Objekt und fügt die Daten hinzu
            val values = ContentValues().apply {
                put(DBContracts.LehrerContract.COLUMN_KUERZEL, kuerzel)
                put(DBContracts.LehrerContract.COLUMN_NACHNAME, nachname)
                if(vorname != null) put(DBContracts.LehrerContract.COLUMN_VORNAME, vorname)
                put(DBContracts.LehrerContract.COLUMN_GESCHLECHT, geschlecht)
            }

            //Erstellt eine neue Zeile in der DB mit den Daten von values
            db.insert(DBContracts.LehrerContract.TABLE_NAME, null, values)

            db.close()
        }

        /**
         * @param context Context der App - muss übergeben werden, um DB-Connection aufzubauen
         * @param kuerzel Das Kürzel der Lehrkraft von welcher man die Informationen haben möchte
         * @return LehrerData: ein Objekt gefüllt mit den Daten des spezifischen Lehrers
         */
        fun getLehrerInformation(context: Context, kuerzel: String) : LehrerData {

            val db = DataBaseHelper(context).writableDatabase

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
                db.close()
                return result
            } else cursor.close(); db.close(); return LehrerData(kuerzel, "FEHLER", "FEHLER", "FEHLER")
        }
    }
}