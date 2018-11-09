package de.codecrops.vertretungsplangymwen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import de.codecrops.vertretungsplangymwen.sqlite.DBContracts
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dbman = DBManager(this.applicationContext)
        dbman.addDataToVertretungsDB("8b", 1, "PFA", "Informatik", "2.16", "nix")
        if(dbman.hasPupilVertretung("8b") && dbman.getVertretungenByKlasse("8b").size > 0) testtext.text = dbman.getVertretungenByKlasse("8b")[0].toString()

        dbman.addLehrerToDB("PFA", "Pfahler", "Sonja", "weiblich")

        if(dbman.isLehrerAlreadyInDB("PFA")) testtext.text = dbman.getLehrerInformation("PFA").toString()

        //text.setText(HttpPull.getToday())
    }
}
