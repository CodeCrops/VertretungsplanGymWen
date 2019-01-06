package de.codecrops.vertretungsplangymwen

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.codecrops.vertretungsplangymwen.credentials.CredentialsManager
import de.codecrops.vertretungsplangymwen.network.PasswordCheck
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val cm = CredentialsManager

        if(PasswordCheck.isCorrect
                ("${cm.getHTTPUsername(this)}:${cm.getHTTPPassword(this)}")) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
            cm.deleteHTTPCredentials(this)
        }

        login.setOnClickListener { login() }
    }

    private fun login() {
        val name = username.text
        val pw = password.text
        if(PasswordCheck.isCorrect("$name:$pw")) {
            //richtiges Passwort
            CredentialsManager.setHTTPCredentials(this, name.toString(), pw.toString())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            //falsches Passwort
        }
    }
}