package de.codecrops.vertretungsplangymwen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import de.codecrops.vertretungsplangymwen.data.DataPull
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val e = DataPull.extractTomorrow()
        if(e.unauthorized!=true) {
            text.setText(e.table[0].toString())
        }
    }
}
