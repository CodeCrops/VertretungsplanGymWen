package de.codecrops.vertretungsplangymwen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.codecrops.vertretungsplangymwen.network.PasswordCheck
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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