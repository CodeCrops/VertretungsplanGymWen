package de.codecrops.vertretungsplangymwen.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import de.codecrops.vertretungsplangymwen.data.VertretungData
import java.text.SimpleDateFormat
import java.util.*

class DBManager {

    companion object {

        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param klasse Die Klasse, welche die Vertretung betrifft (in Kursen ist dies die Kursnummer)
         * @param stunde Die Stunde, in welcher die Vertretung stattfindet
         * @param vertretung Die Lehrkraft, welche die Vertretung durchführt (evtl. auch hier "entfällt XYZ")
         * @param fach Das Fach, welches anstelle des ursprünglichen Fachs unterrichtet wird (nur nötig, wenn es entfällt)
         * @param raum Der Raum, in welchem anstelle des ursprünglichen Raums unterrichtet wird (nicht nötig, wenn es entfällt)
         * @param sonstiges Sonstige Informationen
         * @param datum Datum der Vertretungsstunde
         */
        fun addVertretungsstunde(context: Context, klasse: String, stunde: Int, vertretung: String, fach: String?, raum: String?, sonstiges: String?, datum: Date) {
            /*
            ACHTUNG:

                - Die Parameter klasse, stunde und vertretung (&datum) sind nicht nullable -> sie müssen übergeben werden!
                - Die Paramter fach, raum und sonstiges sind nullable -> sie können weggelassen werden und sollten dies auch bei leeren Werten bitte werden! Kein "" anstatt null
             */

            //Datum/Kalendar
            val date = Calendar.getInstance()
            date.time = datum
            val timestamp = "${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH)}.${date.get(Calendar.YEAR)}"

            //Erstellt ein ContentValues-Objekt und fügt die Daten hinzu
            val values = ContentValues().apply {
                put(DBContracts.PlanContract.COLUMN_KLASSE, klasse)
                put(DBContracts.PlanContract.COLUMN_STUNDE, stunde)
                put(DBContracts.PlanContract.COLUMN_VERTRETUNG, vertretung)
                if(fach != null) put(DBContracts.PlanContract.COLUMN_FACH, fach)
                if(raum != null) put(DBContracts.PlanContract.COLUMN_RAUM, raum)
                if(sonstiges != null) put(DBContracts.PlanContract.COLUMN_SONSTIGES, sonstiges)
                put(DBContracts.PlanContract.COLUMN_DATE, timestamp)
            }

            //Erstellt eine neue Zeile in der DB mit den Daten von values
            context.contentResolver.insert(DBContracts.PlanContract.CONTENT_URI, values)
        }

        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param klasse Klasse
         * @param datum Datum des durchsuchten Tages
         * @return Cursor
         */
        private fun getPlanCursorByKlasse(context: Context, klasse: String, datum: Date): Cursor {

            //Datum/Kalendar
            val date = Calendar.getInstance()
            date.time = datum
            val timestamp = "${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH)}.${date.get(Calendar.YEAR)}"

            //Erstellt die Selection (die WHERE-Clause)
            val selection = "${DBContracts.PlanContract.COLUMN_KLASSE} = '$klasse' AND ${DBContracts.PlanContract.COLUMN_DATE} = '$timestamp'"

            //Erhalte den Cursor von der DB
            val cursor = context.contentResolver.query(DBContracts.PlanContract.CONTENT_URI, arrayOf("*"), selection, null, null)

            //Gebe den Cursor zurück
            return cursor
        }

        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param datum Datum des durchsuchten Tages
         * @return Cursor
         */
        private fun getPlanCursorByDate(context: Context, datum: Date): Cursor {

            //Datum/Kalendar
            val date = Calendar.getInstance()
            date.time = datum
            val timestamp = "${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH)}.${date.get(Calendar.YEAR)}"

            //Erstellt die Selection (die WHERE-Clause)
            val selection = "${DBContracts.PlanContract.COLUMN_DATE} = '$timestamp'"

            //Erhalte den Cursor von der DB
            val cursor = context.contentResolver.query(DBContracts.PlanContract.CONTENT_URI, arrayOf("*"), selection, null, null)

            //Gebe den Cursor zurück
            return cursor
        }



        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param klasse Die Klasse/der Kurs, in welchem sich der Schüler befindet
         * @param datum Datum des Tages, welcher überprüft werder soll
         * @return Boolean, True : "Schüler hat Vertretung", False : "Schüler hat normal Unterricht"
         */
        fun hasPupilVertretung(context: Context, klasse: String, datum: Date) : Boolean {
            val cursor = getPlanCursorByKlasse(context, klasse, datum)
            if(cursor.moveToNext()) {
                cursor.close()
                return true
            } else cursor.close(); return false
        }

        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param klasse Die Klasse/der Kurs, in welchem sich der Schüler befindet
         * @param datum Datum des Tages, für welchen Vertretungen gefunden werden sollen
         * @return ArrayList<VertretungData> : Eine ArrayList, welche pro element eine Vertretungsstunde enthält
         */
        fun getVertretungenByKlasse(context: Context, klasse: String, datum: Date): ArrayList<VertretungData> {

            //result wird vorbereitet
            val result = ArrayList<VertretungData>()

            //cursor wird von DB geholt
            val cursor = getPlanCursorByKlasse(context, klasse, datum)

            //while schleife zum bearbeiten des Cursors
            while (cursor.moveToNext()) {
                result.add( //füget result die neue Vertretungsstunde hinzu
                        VertretungData( //erstellt ein neues Objekt von VertretungData
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

        fun getAllVertretungen(context: Context, datum: Date) : ArrayList<VertretungData> {
            //Datum/Kalendar
            val date = Calendar.getInstance()
            date.time = datum
            val timestamp = "${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH)}.${date.get(Calendar.YEAR)}"

            //result wird vorbereitet
            val result = ArrayList<VertretungData>()

            //cursor wird von DB geholt
            val cursor = getPlanCursorByDate(context, datum)

            //while schleife zum bearbeiten des Cursors
            while (cursor.moveToNext()) {
                result.add( //füget result die neue Vertretungsstunde hinzu
                        VertretungData( //erstellt ein neues Objekt von VertretungData
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
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param kuerzel Das Kürzel der Lehrkraft, welche überprüft wird
         * @return Boolean - true:ist bereits enthalten ; false:noch nicht enthalten
         */
        fun isLehrerInDB(context: Context, kuerzel: String) : Boolean {

            val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '${kuerzel.toLowerCase()}'"
            val cursor = context.contentResolver.query(DBContracts.LehrerContract.CONTENT_URI, arrayOf("*"), selection, null, null)
            if(cursor.moveToNext()) {
                cursor.close()
                return true
            } else cursor.close(); return false
        }

        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param kuerzel Das Kürzel des Lehrers, welcher entfernt werden soll
         */
        fun removeLehrerFromDB(context: Context, kuerzel: String) {
            val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '${kuerzel.toLowerCase()}'"
            context.contentResolver.delete(DBContracts.LehrerContract.CONTENT_URI, selection, null)
        }

        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param values ContentValues des neuen Lehrers
         */
        fun updateLehrerInDB(context: Context, values: ContentValues) {
            val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '${values.getAsString(DBContracts.LehrerContract.COLUMN_KUERZEL)}'"
            context.contentResolver.update(DBContracts.LehrerContract.CONTENT_URI, values, selection, null)
        }

        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param kuerzel Das Kürzel der Lehrkraft
         * @param nachname Der Nachname der Lehrkraft
         * @param vorname Der Vorname der Lehrkraft (nullable, falls nicht bekannt)
         * @param geschlecht Das Geschlecht der Lehrkraft
         */
        fun addLehrerToDB(context: Context, kuerzel: String, nachname: String, vorname: String?, geschlecht: String) {

            //Erstellt ein ContentValues-Objekt und fügt die Daten hinzu
            val values = ContentValues().apply {
                put(DBContracts.LehrerContract.COLUMN_KUERZEL, kuerzel.toLowerCase())
                put(DBContracts.LehrerContract.COLUMN_NACHNAME, nachname)
                if(vorname != null) put(DBContracts.LehrerContract.COLUMN_VORNAME, vorname)
                put(DBContracts.LehrerContract.COLUMN_GESCHLECHT, geschlecht)
            }

            //Lehrer wird geupdatet, falls er bereits vorhanden ist
            if(isLehrerInDB(context, kuerzel)) {
                updateLehrerInDB(context, values)
                return
            //ist noch nicht vorhanden
            } else {
                context.contentResolver.insert(DBContracts.LehrerContract.CONTENT_URI, values)
            }
        }

        /**
         * @param context Context der App - muss übergeben werden, um auf den contentResolver zuzugreifen
         * @param kuerzel Das Kürzel der Lehrkraft von welcher man die Informationen haben möchte
         * @return LehrerData: ein Objekt gefüllt mit den Daten des spezifischen Lehrers
         */
        fun getLehrerInformation(context: Context, kuerzel: String) : LehrerData {

            val selection = "${DBContracts.LehrerContract.COLUMN_KUERZEL} = '$kuerzel'"
            val cursor = context.contentResolver.query(DBContracts.LehrerContract.CONTENT_URI, arrayOf("*"), selection, null, null)
            if(cursor.moveToNext()) {
                val result = LehrerData(
                        kuerzel,
                        cursor.getString(cursor.getColumnIndex(DBContracts.LehrerContract.COLUMN_NACHNAME)),
                        cursor.getString(cursor.getColumnIndex(DBContracts.LehrerContract.COLUMN_VORNAME)),
                        cursor.getString(cursor.getColumnIndex(DBContracts.LehrerContract.COLUMN_GESCHLECHT))
                )
                cursor.close()
                return result
            } else cursor.close(); return LehrerData(kuerzel, kuerzel, "", "") //kuerzel wird auch als Nachname ausgegeben, damit es zur Not nicht auffällt (sollte ja eh nicht auftreten)
        }

        /**
         * @param context Kontext der App
         */
        fun clearVertretungsDB(context: Context) {
            context.contentResolver.delete(DBContracts.PlanContract.CONTENT_URI, null, null)
        }

        /**
         * @param context Kontext der App
         */
        fun clearVertretungsDB(context: Context, datum: Date) {
            //Datum/Kalendar
            val date = Calendar.getInstance()
            date.time = datum
            val timestamp = "${date.get(Calendar.DAY_OF_MONTH)}.${date.get(Calendar.MONTH)}.${date.get(Calendar.YEAR)}"

            //Selektion
            val selection = "${DBContracts.PlanContract.COLUMN_DATE}='$timestamp'"

            context.contentResolver.delete(DBContracts.PlanContract.CONTENT_URI, selection, null)
        }
    }
}