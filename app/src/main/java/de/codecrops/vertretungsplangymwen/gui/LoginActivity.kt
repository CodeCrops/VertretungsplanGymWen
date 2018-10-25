package de.codecrops.vertretungsplangymwen.gui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.codecrops.vertretungsplangymwen.R
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
    }

    fun login() {
        if(username.equals("") && password.equals("")) {

        }
    }
}