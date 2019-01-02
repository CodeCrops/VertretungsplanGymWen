package de.codecrops.vertretungsplangymwen

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import de.codecrops.vertretungsplangymwen.credentials.CredentialsManager
import de.codecrops.vertretungsplangymwen.sqlite.DBContracts
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //CredentialsManager.setHTTPCredentials(this.applicationContext, "alle", "hitzefrei?")
        /*testtext.text = "Gespeicherte Daten von CredentialsManager: \n" +
                        "- Nutzername: ${CredentialsManager.getHTTPUsername(this.applicationContext)} \n" +
                        "- Passwort:   ${CredentialsManager.getHTTPPassword(this.applicationContext)}"
        */
    }
}
