package de.codecrops.vertretungsplangymwen.sqlite

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import java.lang.IllegalStateException

class DataProvider :  ContentProvider() {

    private lateinit var mDBHelper : DataBaseHelper

    private val LEHRER = 100
    private val LEHRER_ID = 101
    private val VERTRETUNGSPLAN = 102
    private val VERTRETUNGSPLAN_ID = 103
    private val PREFERENCES = 104
    private val PREFERENCES_ID = 105

    //Name, welcher bei Fehlermeldungen mit ausgegeben wird
    private val LOG_TAG = this.javaClass.simpleName

    private val sUriMatcher : UriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        sUriMatcher.addURI(DBContracts.CONTENT_AUTHORITY, DBContracts.PATH_LEHRER, LEHRER)
        sUriMatcher.addURI(DBContracts.CONTENT_AUTHORITY, DBContracts.PATH_LEHRER + "/#", LEHRER_ID)
        sUriMatcher.addURI(DBContracts.CONTENT_AUTHORITY, DBContracts.PATH_SCHEDULE, VERTRETUNGSPLAN)
        sUriMatcher.addURI(DBContracts.CONTENT_AUTHORITY, DBContracts.PATH_SCHEDULE + "/#", VERTRETUNGSPLAN_ID)
        sUriMatcher.addURI(DBContracts.CONTENT_AUTHORITY, DBContracts.PATH_PREFERENCES, PREFERENCES)
        sUriMatcher.addURI(DBContracts.CONTENT_AUTHORITY, DBContracts.PATH_PREFERENCES + "/#", PREFERENCES_ID)
    }

    override fun onCreate(): Boolean {
        mDBHelper = DataBaseHelper(context)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        val db = mDBHelper.readableDatabase

        val cursor : Cursor

        //Figure out if the UriMatcher can match the URI to a specific Code
        val match = sUriMatcher.match(uri)

        when(match) {
            LEHRER -> cursor = db.query(DBContracts.LehrerContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            LEHRER_ID -> {
                val localselection = DBContracts.LehrerContract._ID + "=?"
                // HIER HAB ICH DAS EINFACH WEGGELASSEN, da es nicht funktioniert hat :D Sollte auch ohne gehen: selectionArgs = Array<String>(ContentUris.parseId(uri).toInt()) { ContentUris.parseId(uri).toString()}
                cursor = db.query(DBContracts.LehrerContract.TABLE_NAME, projection, localselection, selectionArgs, null, null, sortOrder)
            }
            VERTRETUNGSPLAN -> cursor = db.query(DBContracts.PlanContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            VERTRETUNGSPLAN_ID -> {
                val localselection = DBContracts.PlanContract._ID + "=?"
                // HIER HAB ICH DAS EINFACH WEGGELASSEN, da es nicht funktioniert hat :D Sollte auch ohne gehen: selectionArgs = Array<String>(ContentUris.parseId(uri).toInt()) { ContentUris.parseId(uri).toString()}
                cursor = db.query(DBContracts.PlanContract.TABLE_NAME, projection, localselection, selectionArgs, null, null, sortOrder)
            }
            PREFERENCES -> cursor = db.query(DBContracts.PreferencesContract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
            PREFERENCES_ID -> {
                val localselection = DBContracts.PreferencesContract._ID + "=?"
                // HIER HAB ICH DAS EINFACH WEGGELASSEN, da es nicht funktioniert hat :D Sollte auch ohne gehen: selectionArgs = Array<String>(ContentUris.parseId(uri).toInt()) { ContentUris.parseId(uri).toString()}
                cursor = db.query(DBContracts.PreferencesContract.TABLE_NAME, projection, localselection, selectionArgs, null, null, sortOrder)
            }

            else -> throw IllegalArgumentException("Cannot query unknown URI $uri!")     //Hier muss einen Exception geworfen werden, da sonst cursor nicht funktioniert (außerdem kommen hier eh keine falschen daten an)

        }

        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues): Uri? {
        val match = sUriMatcher.match(uri)
        when(match) {
            LEHRER -> return insertLehrer(uri, values)
            VERTRETUNGSPLAN -> return insertVertretungsplan(uri, values)
            PREFERENCES -> return insertPreferences(uri, values)
            else -> throw IllegalArgumentException("Insertion is not supported for $uri")
        }
    }

    private fun insertLehrer(uri: Uri, values: ContentValues) : Uri? {
        //Sanity-Check
        var invalidData = false
        //Lehrer-Kuerzel-Validation
        val kuerzelValue = values.getAsString(DBContracts.LehrerContract.COLUMN_KUERZEL)
        if(kuerzelValue.isNullOrBlank() || kuerzelValue.length != 3) {
            invalidData = true
        }
        //Nachname-Kuerzel-Validation
        val nachnameValue = values.getAsString(DBContracts.LehrerContract.COLUMN_NACHNAME)
        if(nachnameValue.isNullOrBlank()) {
            invalidData = true
        }
        //Date-Validation
        val dateValue = values.getAsString(DBContracts.LehrerContract.COLUMN_DATE)
        if(dateValue.isNullOrBlank()) {
            invalidData = true
        }
        //invalidData-Check
        if(invalidData) {
            //throw java.lang.IllegalArgumentException("Failed to insert new Lehrer into DB! Invalid Values!")   <-- weggelassen, um ein crashen der app zu verhindern (mit Log.e ersetzt)
            Log.e(LOG_TAG, "Failed to insert new Lehrer into DB! Invalid Values!")
            return null
        }

        val db = mDBHelper.writableDatabase
        val id : Long = db.insert(DBContracts.LehrerContract.TABLE_NAME, null, values)

        //db.insert() fehlgeschlagen:
        if(id.equals(-1)) {
            Log.e(LOG_TAG, "Failed to insert row for $uri")
            return null
        }

        //db.insert() hat funktioniert
        return ContentUris.withAppendedId(uri, id)
    }

    private fun insertVertretungsplan(uri: Uri, values: ContentValues) : Uri? {
        //Sanity-Check
        var invalidData = false
        //Klasse-Validation
        val klasseValue = values.getAsString(DBContracts.PlanContract.COLUMN_KLASSE)
        if(klasseValue.isNullOrBlank()) {
            invalidData = true
        }
        //Stunde-Validation
        val stundeValue = values.getAsInteger(DBContracts.PlanContract.COLUMN_STUNDE)
        if(stundeValue == null || stundeValue <= 0 || stundeValue > 10 ) {
            invalidData = true
        }
        //Vertretung-Validation
        val vertretungValue = values.getAsString(DBContracts.PlanContract.COLUMN_VERTRETUNG)
        if(vertretungValue.isNullOrBlank()) {
            invalidData = true
        }
        //Date-Validation
        val dateValue = values.getAsString(DBContracts.PlanContract.COLUMN_DATE)
        if(dateValue.isNullOrBlank()) {
            invalidData = true
        }
        //invalidData-Check
        if(invalidData) {
            //throw java.lang.IllegalArgumentException("Failed to insert new Vertretungsplan (Stunde) into DB! Invalid Values!")   <-- weggelassen, um ein crashen der app zu verhindern (mit Log.e ersetzt)
            Log.e(LOG_TAG, "Failed to insert new Vertretungsplan (Stunde) into DB! Invalid Values!")
        }

        val db = mDBHelper.writableDatabase
        val id : Long = db.insert(DBContracts.PlanContract.TABLE_NAME, null, values)

        //db.insert() fehlgeschlagen:
        if(id.equals(-1)) {
            Log.e(LOG_TAG, "Failed to insert row for $uri")
            return null
        }

        //db.insert() hat funktioniert
        return ContentUris.withAppendedId(uri, id)
    }

    private fun insertPreferences(uri: Uri, values: ContentValues) : Uri? {
        //Sanity-Checks
        var invalidData = false

        //Kurs-Validation
        val kursValue = values.getAsString(DBContracts.PreferencesContract.COLUMN_KURS)
        if(kursValue.isNullOrBlank()) {
            invalidData = true
        }
        //Type-Validation
        val typeValue = values.getAsInteger(DBContracts.PreferencesContract.COLUMN_TYPEOFKURS)
        if(typeValue == null) {
            invalidData = true
        }

        if(invalidData) {
            Log.e(LOG_TAG, "Failed to insert new Preference into DB! Invalid Values!")
            return null
        }

        val db = mDBHelper.writableDatabase
        val id : Long = db.insert(DBContracts.PreferencesContract.TABLE_NAME, null, values)

        //db.insert() fehlgeschlagen:
        if(id.equals(-1)) {
            Log.e(LOG_TAG, "Failed to insert row for $uri")
            return null
        }

        //db.insert() hat funktioniert
        return ContentUris.withAppendedId(uri, id)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val db = mDBHelper.writableDatabase
        val match = sUriMatcher.match(uri)
        when(match) {
            LEHRER -> return db.delete(DBContracts.LehrerContract.TABLE_NAME, selection, selectionArgs)
            LEHRER_ID -> {
                val localselection = DBContracts.LehrerContract._ID + "=?"
                val localselectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return db.delete(DBContracts.LehrerContract.TABLE_NAME, localselection, localselectionArgs)
            }
            VERTRETUNGSPLAN -> return db.delete(DBContracts.PlanContract.TABLE_NAME, selection, selectionArgs)
            VERTRETUNGSPLAN_ID -> {
                val localselection = DBContracts.PlanContract._ID + "=?"
                val localselectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return db.delete(DBContracts.PlanContract.TABLE_NAME, localselection, localselectionArgs)
            }
            PREFERENCES -> return db.delete(DBContracts.PreferencesContract.TABLE_NAME, selection, selectionArgs)
            PREFERENCES_ID -> {
                val localselection = DBContracts.PreferencesContract._ID + "=?"
                val localselectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return db.delete(DBContracts.PreferencesContract.TABLE_NAME, localselection, localselectionArgs)
            }
            else -> throw java.lang.IllegalArgumentException("Deletion is not supportet for $uri")     //Hier muss einen Exception geworfen werden (außerdem kommen hier eh keine falschen daten an)
        }
    }

    override fun update(uri: Uri, values: ContentValues, selection: String?, selectionArgs: Array<String>?): Int {
        val match = sUriMatcher.match(uri)
        when(match) {
            LEHRER -> return updateLehrer(values, selection, selectionArgs)
            LEHRER_ID -> {
                val localselection = DBContracts.LehrerContract._ID + "=?"
                val localselectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return updateLehrer(values, localselection, localselectionArgs)
            }
            VERTRETUNGSPLAN -> return updateVertretungsplan(values, selection, selectionArgs)
            VERTRETUNGSPLAN_ID -> {
                val localselection = DBContracts.LehrerContract._ID + "=?"
                val localselectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return updateVertretungsplan(values, localselection, localselectionArgs)
            }
            PREFERENCES -> return updatePreferences(values, selection, selectionArgs)
            PREFERENCES_ID -> {
                val localselection = DBContracts.PreferencesContract._ID + "=?"
                val localselectionArgs = arrayOf(ContentUris.parseId(uri).toString())
                return updateLehrer(values, localselection, localselectionArgs)
            }
            else -> throw java.lang.IllegalArgumentException("Update is not supported for $uri")
        }
    }

    private fun updateLehrer(values: ContentValues, selection: String?, selectionArgs: Array<String>?) : Int {
        //Sanity-Check
        //Überprüft, ob überhaupt ein Kürzel geupdatet werden soll. Wenn ja -> SanityCheck. Wenn nein -> nichts, da altes Kürzel aus DB eh bestehen bleibt
        if(values.containsKey(DBContracts.LehrerContract.COLUMN_KUERZEL)) {
            //Kürzel-Check (Muss genau 3 Zeichen lang sein)
            if(values.getAsString(DBContracts.LehrerContract.COLUMN_KUERZEL).length != 3) {
                System.err.print("[DataProvider] Lehrer-Kürzel muss genau 3 Zeichen lang sein!")
                return 0
            }
        }
        //Überprüft, ob überhaupt ein Nachname geupdatet werden soll. Wenn ja -> SanityCheck. Wenn nein -> nichts, da alter Nachname aus DB eh bestehen bleibt
        if(values.containsKey(DBContracts.LehrerContract.COLUMN_NACHNAME)) {
            //Nachname-Check (Darf nicht leer oder null sein)
            if(values.getAsString(DBContracts.LehrerContract.COLUMN_NACHNAME).isNullOrBlank()) {
                System.err.print("[DataProvider] Lehrer-Nachname darf nicht leer sein!")
                return 0
            }
        }

        //DB-Update
        return mDBHelper.writableDatabase.update(DBContracts.LehrerContract.TABLE_NAME, values, selection, selectionArgs)
    }

    private fun updateVertretungsplan(values: ContentValues, selection: String?, selectionArgs: Array<String>?) : Int {
        //Sanity-Check
        //Klasse-Check entfällt, da es auch leere "Klassen" gibt
        //Überprüft, ob überhaupt eine Stunde geupdatet werden soll. Wenn ja -> SanityCheck. Wenn nein -> nichts, da alte Stunde aus DB eh bestehen bleibt
        if(values.containsKey(DBContracts.PlanContract.COLUMN_STUNDE)) {
            //Stunde-Check (Darf nicht 0 oder null sein)
            if(values.getAsInteger(DBContracts.PlanContract.COLUMN_STUNDE) == 0) {
                System.err.print("[DataProvider] Plan-Stunde darf nicht 0 sein!")
                return 0
            }
        }
        //Überprüft, ob überhaupt eine Vertretung geupdatet werden soll. Wenn ja -> SanityCheck. Wenn nein -> nichts, da alte Vertretung aus DB eh bestehen bleibt
        if(values.containsKey(DBContracts.PlanContract.COLUMN_VERTRETUNG)) {
            //Vertretung-Check (Darf nicht leer oder null sein)
            if(values.getAsString(DBContracts.PlanContract.COLUMN_VERTRETUNG).isNullOrBlank()) {
                System.err.print("[DataProvider] Plan-Vertretung darf nicht leer oder null sein!")
                return 0
            }
        }
        //DB-Update
        return mDBHelper.writableDatabase.update(DBContracts.PlanContract.TABLE_NAME, values, selection, selectionArgs)
    }

    private fun updatePreferences(values: ContentValues, selection: String?, selectionArgs: Array<String>?) : Int {
        //Sanity-Check
        //Kurs-Validation
        if(values.containsKey(DBContracts.PreferencesContract.COLUMN_KURS)) {
            if(values.getAsString(DBContracts.PreferencesContract.COLUMN_KURS).isNullOrBlank()) {
                return 0
            }
        }

        //Type-Validation
        if(!values.containsKey(DBContracts.PreferencesContract.COLUMN_TYPEOFKURS)) {
            return 0
        }

        //DB-Update
        return mDBHelper.writableDatabase.update(DBContracts.PreferencesContract.TABLE_NAME, values, selection, selectionArgs)
    }

    override fun getType(uri: Uri): String? {
        val match = sUriMatcher.match(uri)
        when(match) {
            LEHRER -> return DBContracts.LehrerContract.CONTENT_LIST_TYPE
            LEHRER_ID -> return DBContracts.LehrerContract.CONTENT_ITEM_TYPE
            VERTRETUNGSPLAN -> return DBContracts.PlanContract.CONTENT_LIST_TYPE
            VERTRETUNGSPLAN_ID -> return DBContracts.PlanContract.CONTENT_ITEM_TYPE
            PREFERENCES -> return DBContracts.PreferencesContract.CONTENT_LIST_TYPE
            PREFERENCES_ID -> return DBContracts.PreferencesContract.CONTENT_ITEM_TYPE
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
    }

}