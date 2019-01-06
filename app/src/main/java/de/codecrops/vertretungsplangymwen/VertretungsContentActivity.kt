package de.codecrops.vertretungsplangymwen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.View
import kotlinx.android.synthetic.main.activity_vertretungs_content.*

class VertretungsContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertretungs_content)
        val b = intent.extras!!
        klasse.text = b.getString("klasse")
        fach.text = b.getString("fach")
        raum.text = b.getString("raum")

        if(b.getString("vertretung").contains("entfällt")) {
            vertretung.text = resources.getString(R.string.entfällt)
            app_bar.setBackgroundColor(ContextCompat.getColor(this, R.color.entfallRed))
        } else {
            vertretung.text = b.getString("vertretung")
        }

        if(b.getString("kommentar").equals("")) {
            kommentarView.visibility = View.INVISIBLE
            kommentar.visibility = View.INVISIBLE
        } else {
            kommentar.text = b.getString("kommentar")
        }

        val toolbar: Toolbar = toolbar
        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = "${b.getInt("stunde").toString()}. Stunde"
        }
        toolbar.setNavigationOnClickListener { super.onBackPressed() }
    }
}
