package de.codecrops.vertretungsplangymwen.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VertretungsplanDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    //ACHTUNG: Sollte der Contract geändert werden, muss hier DB_VERSION erhöht werden!

    companion object {
        //Einstellungen
        val DB_VERSION = 1
        val DB_NAME = "VertretungsPlan.db"

        //Konstante zur Erstellung des TABLES aus PlanContract
        private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContracts.PlanContract.TABLE_NAME + " (" +
                        DBContracts.PlanContract.COLUMN_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DBContracts.PlanContract.COLUMN_KLASSE + " TEXT NOT NULL," +
                        DBContracts.PlanContract.COLUMN_STUNDE + " INTEGER NOT NULL," +
                        DBContracts.PlanContract.COLUMN_VERTRETUNG + " TEXT NOT NULL," +
                        DBContracts.PlanContract.COLUMN_FACH + " TEXT," +
                        DBContracts.PlanContract.COLUMN_RAUM + " TEXT," +
                        DBContracts.PlanContract.COLUMN_SONSTIGES + " TEXT" +
                        ")"

        //Konstante zur Löschung aller Inhalte der DB
        private const val SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " +
                        DBContracts.PlanContract.TABLE_NAME
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