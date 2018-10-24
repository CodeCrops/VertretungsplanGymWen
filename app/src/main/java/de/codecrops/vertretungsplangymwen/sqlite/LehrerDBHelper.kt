package de.codecrops.vertretungsplangymwen.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LehrerDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    //ACHTUNG: Sollte der Contract geändert werden, muss hier DB_VERSION erhöht werden!

    companion object {
        //Einstellungen
        val DB_VERSION = 1
        val DB_NAME = "VertretungsPlanApp.db"

        //Konstante zur Erstellung des TABLES aus PlanContract
        private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContracts.LehrerContract.TABLE_NAME + " (" +
                        DBContracts.LehrerContract.COLUMN_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DBContracts.LehrerContract.COLUMN_KUERZEL + " TEXT NOT NULL, " +
                        DBContracts.LehrerContract.COLUMN_NACHNAME + " TEXT NOT NULL, " +
                        DBContracts.LehrerContract.COLUMN_VORNAME + " TEXT, " +
                        DBContracts.LehrerContract.COLUMN_GESCHLECHT + " TEXT, " +
                        ")"

        //Konstante zur Löschung aller Inhalte der DB
        private const val SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " +
                        DBContracts.LehrerContract.TABLE_NAME
    }

    /**
     * @param db Die Datenbank, auf welcher der VertretungsTABLE erstellt werden soll
     */
    override fun onCreate(db: SQLiteDatabase) {
        //Erstellt in der DB die TABLES usw. (Init für DB)
        db.execSQL(SQL_CREATE_ENTRIES)
    }
    /**
     * @param db Die Datenbank, auf welcher der VertretungsTABLE aktualisiert werden soll
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Flusht alte DB und erstellt neue TABLES usw (mithilfe von onCreate())
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }



}