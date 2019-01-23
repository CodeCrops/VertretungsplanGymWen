package de.codecrops.vertretungsplangymwen.gui

import android.content.Context
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import de.codecrops.vertretungsplangymwen.KlasseActivity
import de.codecrops.vertretungsplangymwen.R.layout.klasse_list_item
import kotlinx.android.synthetic.main.klasse_list_item.view.*

class KlasseAdapter(private val klasseActivity: KlasseActivity, data: ArrayList<String?>, context: Context) : ArrayAdapter<String?>(context, klasse_list_item, data) {

    private class ViewHolder {
        internal var course: EditText? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data: String? = getItem(position)
        lateinit var viewHolder: KlasseAdapter.ViewHolder
        lateinit var view: View

        if(convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(klasse_list_item, parent, false) as View
            viewHolder.course = view.course_editText
            view.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }

        addOnClickListeners(view, viewHolder)

        if(data != null) {
            viewHolder.course!!.text = SpannableStringBuilder(data)
        }
        return view
    }

    private fun addOnClickListeners(view: View?, viewHolder: ViewHolder) {
        view!!.checkButton.setOnClickListener {
            //TODO: In Datenbank schreiben
        }
        view!!.addButton.setOnClickListener {
            if(viewHolder.course!!.text != null) {
                if(!viewHolder.course!!.text.isEmpty()) {
                    klasseActivity.addItem()
                    view!!.addButton.visibility = View.INVISIBLE
                }
            }
        }
    }
}