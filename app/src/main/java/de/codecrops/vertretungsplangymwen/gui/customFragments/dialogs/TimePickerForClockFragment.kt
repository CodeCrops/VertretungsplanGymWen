package de.codecrops.vertretungsplangymwen.gui.customFragments.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.widget.TextView
import android.widget.TimePicker
import de.codecrops.vertretungsplangymwen.ClockSettingsActivity
import de.codecrops.vertretungsplangymwen.refresh.RefreshManager
import de.codecrops.vertretungsplangymwen.settings.SettingsManager
import java.util.*

private val LOG_TAG = "TimePickerFCFrag"

class TimePickerForClockFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Using Now-Time as default values
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        //Create new instance of TimePickerDialog
        val tpd = TimePickerDialog(activity, AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, true)

        //Custom Title
        val tvTitle = TextView(activity)
        tvTitle.text = "Wann soll aktualisiert werden?"
        tvTitle.setPadding(5, 3, 5, 3)
        tvTitle.gravity = Gravity.CENTER_HORIZONTAL
        tpd.setCustomTitle(tvTitle)

        //return TPD
        return tpd
    }

    //Callback Method
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        //Adding new Refresh-Time to Settings-DB
        val oldVal = SettingsManager.getBackgroundRefreshAutoClock(context!!)
        var newh = "$hourOfDay"
        var newm = "$minute"
        if(hourOfDay<10) {
            newh = "0$hourOfDay"
        }
        if(minute<10) {
            newm = "0$minute"
        }

        if(oldVal.isNullOrBlank()) {
            SettingsManager.setBackgroundRefreshAutoClock(context!!, "$newh:$newm")
        } else {
            val newvalue = oldVal + "//" + newh + ":" + newm
            SettingsManager.setBackgroundRefreshAutoClock(context!!, newvalue)
        }



        //Restarting Activity in order to show the new data
        val intent = Intent(context, ClockSettingsActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }
}