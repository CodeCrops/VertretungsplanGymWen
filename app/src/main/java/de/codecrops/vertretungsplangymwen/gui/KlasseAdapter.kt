package de.codecrops.vertretungsplangymwen.gui

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import de.codecrops.vertretungsplangymwen.KlasseActivity
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.R.layout.klasse_list_item
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import de.codecrops.vertretungsplangymwen.sqlite.PREFERENCETYPE
import kotlinx.android.synthetic.main.klasse_list_item.view.*

class KlasseAdapter(private val klasseActivity: KlasseActivity, data: ArrayList<String>, context: Context) : ArrayAdapter<String>(context, klasse_list_item, data) {

    private class ViewHolder {
        internal var course: TextView? = null
    }

    //TODO: ADD BUTTON NUR BEIM LETZTEN ITEM, ITEM WIRD LEER UND NEUES Hinzugefügt wenn set button gedrückt wird

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data: String = getItem(position)
        lateinit var viewHolder: KlasseAdapter.ViewHolder
        lateinit var view: View

        if(convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(klasse_list_item, parent, false) as View
            viewHolder.course = view.course
            view.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }

        addOnClickListeners(view, viewHolder)

        viewHolder.course!!.text = SpannableStringBuilder(data)

        return view
    }

    private fun addOnClickListeners(view: View?, viewHolder: ViewHolder) {
        view!!.checkOrDeleteButton.setOnClickListener {
            klasseActivity.removeItem(view.course.text.toString())
        }
    }
}