package de.codecrops.vertretungsplangymwen.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    //ACHTUNG: Sollte der Contract geändert werden, muss hier DB_VERSION erhöht werden!

    companion object {
        //Einstellungen
        val DB_VERSION = 4
        val DB_NAME = "VertretungsPlanApp.db"

        //Konstante zur Erstellung des SCHEDULE TABLEs aus PlanContract
        private const val SQL_CREATE_SCHEDULE =
                "CREATE TABLE IF NOT EXISTS " + DBContracts.PlanContract.TABLE_NAME + " (" +
                        DBContracts.PlanContract.COLUMN_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DBContracts.PlanContract.COLUMN_KLASSE + " TEXT NOT NULL, " +
                        DBContracts.PlanContract.COLUMN_STUNDE + " INTEGER NOT NULL, " +
                        DBContracts.PlanContract.COLUMN_VERTRETUNG + " TEXT NOT NULL, " +
                        DBContracts.PlanContract.COLUMN_FACH + " TEXT, " +
                        DBContracts.PlanContract.COLUMN_RAUM + " TEXT, " +
                        DBContracts.PlanContract.COLUMN_SONSTIGES + " TEXT," +
                        DBContracts.PlanContract.COLUMN_DATE + " TEXT" +
                        ")"
        private const val SQL_CREATE_LEHRER =
                "CREATE TABLE IF NOT EXISTS " + DBContracts.LehrerContract.TABLE_NAME + " (" +
                        DBContracts.LehrerContract.COLUMN_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DBContracts.LehrerContract.COLUMN_KUERZEL + " TEXT NOT NULL, " +
                        DBContracts.LehrerContract.COLUMN_NACHNAME + " TEXT NOT NULL, " +
                        DBContracts.LehrerContract.COLUMN_VORNAME + " TEXT, " +
                        DBContracts.LehrerContract.COLUMN_GESCHLECHT + " TEXT," +
                        DBContracts.LehrerContract.COLUMN_DATE + " TEXT" +
                        ")"
        private const val SQL_CREATE_PREFERENCES =
                "CREATE TABLE IF NOT EXISTS " + DBContracts.PreferencesContract.TABLE_NAME + " (" +
                        DBContracts.PreferencesContract.COLUMN_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DBContracts.PreferencesContract.COLUMN_KURS + " TEXT NOT NULL, " +
                        DBContracts.PreferencesContract.COLUMN_TYPEOFKURS + " INTEGER NOT NULL" +
                        ")"

        //Konstante zur Löschung aller Inhalte der DB
        private const val SQL_DELETE_SCHEDULE =
                "DROP TABLE IF EXISTS ${DBContracts.PlanContract.TABLE_NAME}"
        private const val SQL_DELETE_LEHRER =
                "DROP TABLE IF EXISTS ${DBContracts.LehrerContract.TABLE_NAME}"
        private const val SQL_DELETE_PREFERENCES =
                "DROP TABLE IF EXISTS ${DBContracts.LehrerContract.TABLE_NAME}"
    }

    /**
     * @param db Die Datenbank, auf welcher der VertretungsTABLE erstellt werden soll
     */
    override fun onCreate(db: SQLiteDatabase) {
        //Erstellt in der DB die TABLES usw. (Init für DB)
        db.execSQL(SQL_CREATE_SCHEDULE)
        db.execSQL(SQL_CREATE_LEHRER)
        db.execSQL(SQL_CREATE_PREFERENCES)
    }

    /**
     * @param db Die Datenbank, auf welcher der VertretungsTABLE aktualisiert werden soll
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Flusht alte DB und erstellt neue TABLES usw (mithilfe von onCreate())
        db.execSQL(SQL_DELETE_SCHEDULE)
        db.execSQL(SQL_DELETE_LEHRER)
        db.execSQL(SQL_DELETE_PREFERENCES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }



}