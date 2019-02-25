package de.codecrops.vertretungsplangymwen.gui

import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import de.codecrops.vertretungsplangymwen.ClockSettingsActivity
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.settings.SettingsManager
import kotlinx.android.synthetic.main.clock_list_item.view.*
import java.util.*

class ClockAdapter(private val clockSettingsActivity: ClockSettingsActivity, data: ArrayList<String?>, context: Context) : ArrayAdapter<String?>(context, R.layout.clock_list_item, data)  {

    val LOG_TAG = "ClockAdapter"

    private class ViewHolder {
        internal var clock_info: EditText? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data: String? = getItem(position)
        lateinit var viewHolder: ClockAdapter.ViewHolder
        lateinit var view: View

        if(convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(R.layout.clock_list_item, parent, false) as View
            viewHolder.clock_info = view.clock_editText
            view.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }

        addOnClickListeners(view, viewHolder)

        if(data != null) {
            viewHolder.clock_info!!.text = SpannableStringBuilder(data)
        }

        return view
    }

    private fun addOnClickListeners(view: View?, viewHolder: ClockAdapter.ViewHolder) {
        view!!.changeButton.setOnClickListener {
            //TODO: Änderungsfeld aufrufen
        }
        view!!.removeButton.setOnClickListener {
            val text = view.clock_editText.text
            val textSplit = text.split(" ")

            //Success-Info
            val snackbarSuccess = Snackbar.make(view, "Aktualisierung um $text erfolgreich entfernt!", Snackbar.LENGTH_SHORT)
            //Failure-Info
            val snackbarFailure = Snackbar.make(view, "Aktualisierung um $text NICHT entfernt!", Snackbar.LENGTH_SHORT)

            if(SettingsManager.removeTimeFromSettings(context, textSplit[0])) {
                snackbarSuccess.show()

                //Restarting Activity in order to show the new data
                view!!.postDelayed({
                    val intent = Intent(context, ClockSettingsActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    clockSettingsActivity.startActivity(intent)

                }, 2500)
            } else {
                snackbarFailure.show()
            }




        }
    }
}