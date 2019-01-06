package de.codecrops.vertretungsplangymwen.gui

import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.view.animation.AnimationUtils.loadAnimation
import android.view.animation.Animation
import android.widget.TextView
import android.R.attr.name
import android.content.Context
import android.graphics.Color
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
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.entfallRed))
                } else {
            vertretung = "Vertretung: ${Utils.fromHtml(vertretungData.vertretung)}"
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        }

        viewHolder.klasse!!.text = "Klasse ${Utils.fromHtml(vertretungData.klasse)}"
        viewHolder.stunde!!.text = "${Utils.fromHtml(vertretungData.stunde.toString())}. Stunde"
        viewHolder.vertretung!!.text = vertretung
        viewHolder.fach!!.text = "Fach: ${Utils.fromHtml(vertretungData.fach)}"
        viewHolder.raum!!.text = "Raum: ${Utils.fromHtml(vertretungData.raum)}"
        viewHolder.kommentar!!.text = Utils.fromHtml(vertretungData.kommentar)

        return view
    }
}
