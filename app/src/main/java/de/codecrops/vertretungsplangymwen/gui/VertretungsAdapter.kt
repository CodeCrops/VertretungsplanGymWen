package de.codecrops.vertretungsplangymwen.gui

import android.R.attr.layout_centerInParent
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.Animation
import android.widget.TextView
import android.R.attr.name
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.View
import android.widget.ArrayAdapter
import de.codecrops.vertretungsplangymwen.R
import de.codecrops.vertretungsplangymwen.R.layout.vertretungs_list_item
import de.codecrops.vertretungsplangymwen.Utils
import de.codecrops.vertretungsplangymwen.data.VertretungData
import kotlinx.android.synthetic.main.vertretungs_list_item.view.*


class VertretungsAdapter(data: ArrayList<VertretungData>, context: Context) :
        ArrayAdapter<VertretungData>(context, vertretungs_list_item, data), View.OnClickListener {

    private class ViewHolder {
        internal var klasse: TextView? = null
        internal var stunde: TextView? = null
        internal var vertretung: TextView? = null
        internal var fach: TextView? = null
        internal var raum: TextView? = null
        internal var kommentar: TextView? = null
    }

    override fun onClick(v: View?) {

    }

    @Suppress("DEPRECATION")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val vertretungData: VertretungData = getItem(position)!!
        lateinit var viewHolder: ViewHolder
        lateinit var view: View

        if(convertView == null) {
            viewHolder = ViewHolder()
            val inflater = LayoutInflater.from(context)
            view = inflater.inflate(vertretungs_list_item, parent, false) as View
            viewHolder.klasse = view.klasse
            viewHolder.stunde = view.stunde
            viewHolder.vertretung = view.vertretung
            viewHolder.fach = view.fach
            viewHolder.raum = view.raum
            viewHolder.kommentar = view.kommentar

            view.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            view = convertView
        }

        var vertretung: String

        if(vertretungData.vertretung.contains("entfällt") || vertretungData.vertretung.equals("Entfällt!")) {
            vertretung = context.resources.getString(R.string.entfällt)
            viewHolder.stunde!!.background = context.getDrawable(R.drawable.ic_hour_background_red)
            viewHolder.raum!!.visibility = View.INVISIBLE
            viewHolder.kommentar!!.visibility = View.INVISIBLE

        } else {
            vertretung = "Vertretung: ${Utils.fromHtml(vertretungData.vertretung)}"
            viewHolder.stunde!!.background = context.getDrawable(R.drawable.ic_hour_background)
            viewHolder.raum!!.visibility = View.VISIBLE
            viewHolder.kommentar!!.visibility = View.VISIBLE
        }

        if(vertretungData.fach.isEmpty()) {
            viewHolder.fach!!.visibility = View.INVISIBLE
        } else {
            viewHolder.fach!!.visibility = View.VISIBLE
        }

        if(vertretungData.klasse.isEmpty()) {
            viewHolder.klasse!!.visibility = View.INVISIBLE
        } else if(vertretungData.klasse.length > 3) {
            viewHolder.klasse!!.text = Utils.fromHtml(vertretungData.klasse)
            viewHolder.klasse!!.visibility = View.VISIBLE
        } else {
            viewHolder.klasse!!.text = "Klasse ${Utils.fromHtml(vertretungData.klasse)}"
            viewHolder.klasse!!.visibility = View.VISIBLE
        }

        viewHolder.stunde!!.text = "${Utils.fromHtml(vertretungData.stunde.toString())}.h"
        viewHolder.vertretung!!.text = vertretung
        viewHolder.fach!!.text = "Fach: ${Utils.fromHtml(vertretungData.fach)}"
        viewHolder.raum!!.text = "Raum: ${Utils.fromHtml(vertretungData.raum)}"
        viewHolder.kommentar!!.text = Utils.fromHtml(vertretungData.kommentar)

        return view
    }
}
