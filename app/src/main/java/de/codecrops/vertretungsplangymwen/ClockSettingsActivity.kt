package de.codecrops.vertretungsplangymwen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.util.Log
import de.codecrops.vertretungsplangymwen.gui.ClockAdapter
import de.codecrops.vertretungsplangymwen.gui.customFragments.dialogs.TimePickerForClockFragment
import de.codecrops.vertretungsplangymwen.refresh.RefreshManager
import de.codecrops.vertretungsplangymwen.settings.SettingsManager
import kotlinx.android.synthetic.main.activity_clock_settings.*

private val LOG_TAG = "ClockSetAct"

class ClockSettingsActivity : AppCompatActivity() {

    private val list = arrayListOf<String?>()
    private lateinit var adapter: ClockAdapter
    var adapterSize = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_settings)

        setSupportActionBar(toolbar)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setTitle("Aktualisierungszeitpunkte")
        }

        toolbar.setNavigationOnClickListener { super.onBackPressed() }
        fab.setOnClickListener {
            val dialogFragment = TimePickerForClockFragment()
            dialogFragment.show(supportFragmentManager, "TimePicker")
        }

        adapter = ClockAdapter(this, list, this)
        clock_listView.adapter = adapter

        loadDatesOffDB()

        //Restarting RefreshServices (only need the Clock-Feature, so the init of the Manager is enough)
        RefreshManager(this)
    }

    fun addItem(name: String) {
        list.add(name)
        adapterSize++
        adapter.notifyDataSetChanged()
    }

    private fun loadDatesOffDB() {
        val clockstring = SettingsManager.getBackgroundRefreshAutoClock(this)
        if(clockstring.isNullOrBlank()) {
            Log.i(LOG_TAG, "Stopped inserting Dates from DB - there were none!")
            return
        }
        val splittedStrings : List<String> = clockstring.split("//")
        val correctedStrings = splittedStrings.drop(0)

        //-> add in Adapter
        for(item in correctedStrings) {
            var add = ""
            val list = item.split(":")
            when(list[0].toCharArray().size < 2) { //Adding a 0 in front of 1,2,3,4,5 use in Hours
                true -> add = "0${list[0]}"
                false -> add = list[0]
            }
            add = "$add:"
            when(list[1].toCharArray().size < 2) { //Adding a 0 in front of 1,2,3,4,5 use in Minutes
                true -> add = "${add}0${list[1]}"
                false -> add = "${add}${list[1]}"
            }

            Log.i(LOG_TAG, "Adding '$add' to ClockSettingsAdapter")
            addItem("$add Uhr")
        }

        Log.i(LOG_TAG, "loadDatesOffDB done with ${correctedStrings.size} Dates loaded!")

    }
}
