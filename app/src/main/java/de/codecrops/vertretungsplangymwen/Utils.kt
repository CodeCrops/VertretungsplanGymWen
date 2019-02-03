package de.codecrops.vertretungsplangymwen

import android.content.Context
import android.text.Html
import android.os.Build
import android.text.Spanned
import de.codecrops.vertretungsplangymwen.data.NextDateReturn
import de.codecrops.vertretungsplangymwen.data.VertretungData
import de.codecrops.vertretungsplangymwen.network.HttpGetRequest
import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author K1TR1K
 */
object Utils {
    object DAY {
        const val SUNDAY = "So."
        const val SATURDAY = "Sa."
        const val FRIDAY = "Fr."
    }

    @Suppress("DEPRECATION")
    fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }

    fun dateEqualsToday(d: Date) : Boolean {
        val date = Calendar.getInstance()
        date.time = d
        val calendar = Calendar.getInstance()
        if(date.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                date.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                date.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
            return true
        }
        return false
    }

    fun dateEqualsNextDay(d: Date) : NextDateReturn {
        val date = Calendar.getInstance()
        date.time = d
        val currentCalendar = Calendar.getInstance()
        val currentDate: Date = currentCalendar.time
        val day = SimpleDateFormat("EE", Locale.GERMAN).format(currentDate.time)

        when(day) {
            DAY.FRIDAY -> {
                val dt = date
                dt.add(Calendar.DATE, -3)
                if(dateEqualsToday(dt.time)) return NextDateReturn(d, true)
            }
            DAY.SATURDAY -> {
                val dt = date
                dt.add(Calendar.DATE, -2)
                if(dateEqualsToday(dt.time)) return NextDateReturn(d, true)
            }
            DAY.SUNDAY -> {
                val dt = date
                dt.add(Calendar.DATE, -1)
                if(dateEqualsToday(dt.time)) return NextDateReturn(d, true)
            }
            else -> {
                val dt = date
                dt.add(Calendar.DATE, -1)
                if(dateEqualsToday(dt.time)) return NextDateReturn(d, true)
            }
        }
        return NextDateReturn(d, false)
    }

    fun formGermanDate(c: Calendar) : String {
        return "${c.get(Calendar.DAY_OF_MONTH)}.${c.get(Calendar.MONTH)+1}.${c.get(Calendar.YEAR)}"
    }

    fun fillDatabase(context: Context) {
        //Heute
        val extractToday = HttpGetRequest.extractToday(context)
        if(!(extractToday.unauthorized || extractToday.networkError)) {
            DBManager.clearVertretungsDB(context)
        }
        if(!(extractToday.unauthorized || extractToday.networkError)) {
            if(Utils.dateEqualsToday(extractToday.date)) {
                for(v: VertretungData in extractToday.table) {
                    DBManager.addVertretungsstunde(context, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, extractToday.date)
                }
            }
        }

        //Nächster Tag
        val extractNextDay = HttpGetRequest.extractTomorrow(context)
        if(!(extractNextDay.unauthorized || extractNextDay.networkError)) {
            val nextDateReturn = Utils.dateEqualsNextDay(extractNextDay.date)
            if (nextDateReturn.isNextDay) {
                for (v: VertretungData in extractNextDay.table) {
                    DBManager.addVertretungsstunde(context, v.klasse, v.stunde, v.vertretung, v.fach, v.raum, v.kommentar, nextDateReturn.date)
                }
            }
        }

    }
}