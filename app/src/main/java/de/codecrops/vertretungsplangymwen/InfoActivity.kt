package de.codecrops.vertretungsplangymwen

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {

    //TODO: Den Text verbessern

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(R.string.info)
        }

        toolbar.setNavigationOnClickListener { super.onBackPressed() }

        twitterButton.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://twitter.com/codecrops"))
            startActivity(i)
        }
    }
}
