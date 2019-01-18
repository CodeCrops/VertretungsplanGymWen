package de.codecrops.vertretungsplangymwen

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_help.*

class HelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle(R.string.report_issue)
        }

        toolbar.setNavigationOnClickListener { super.onBackPressed() }

        fab.setOnClickListener {
            if(name.text.isEmpty() || subject.text.isEmpty() || description.text.isEmpty()){
                Toast.makeText(this, getString(R.string.empty_text), Toast.LENGTH_SHORT).show();
            } else {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "message/rfc822"
                //TODO: Email des Empfängers
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(""))
                intent.putExtra(Intent.EXTRA_SUBJECT, "${name.text} | ${subject.text}")
                intent.putExtra(Intent.EXTRA_TEXT, description.text)
                try {
                    startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, getString(R.string.no_email_client), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
