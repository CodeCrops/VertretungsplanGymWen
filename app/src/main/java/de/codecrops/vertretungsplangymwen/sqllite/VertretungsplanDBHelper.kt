package de.codecrops.vertretungsplangymwen.sqllite

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
        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.PlanContract.TABLE_NAME + " (" +
                        DBContract.PlanContract.COLUMN_LISTID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        DBContract.PlanContract.COLUMN_KLASSE + " TEXT NOT NULL," +
                        DBContract.PlanContract.COLUMN_STUNDE + " INTEGER NOT NULL," +
                        DBContract.PlanContract.COLUMN_VERTRETUNG + " TEXT NOT NULL," +
                        DBContract.PlanContract.COLUMN_FACH + " TEXT," +
                        DBContract.PlanContract.COLUMN_RAUM + " TEXT," +
                        DBContract.PlanContract.COLUMN_SONSTIGES + " TEXT" +
                        ")"

        //Konstante zur Löschung aller Inhalte der DB
        private val SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " +
                        DBContract.PlanContract.TABLE_NAME
    }

    override fun onCreate(db: SQLiteDatabase) {
        //Erstellt in der DB die TABLES usw. (Init für DB)
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Flusht alte DB und erstellt neue TABLES usw (mithilfe von onCreate())
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }



}