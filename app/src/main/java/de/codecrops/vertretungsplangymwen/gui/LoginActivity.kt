package de.codecrops.vertretungsplangymwen.gui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.network.PasswordCheck
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        login.setOnClickListener { login() }
    }

    private fun login() {
        val name = username.text
        val pw = password.text
        if(PasswordCheck.isCorrect("$name:$pw")) {
            //richtiges Passwort
        } else {
            //falsches Passwort
        }
    }
}