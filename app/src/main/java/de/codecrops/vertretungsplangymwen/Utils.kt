package de.codecrops.vertretungsplangymwen

import android.text.Html
import android.os.Build
import android.text.Spanned
import android.util.Log
import de.codecrops.vertretungsplangymwen.data.NextDateReturn
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author K1TR1K
 */

object Utils {
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
            "Fr." -> {
                val dt = date
                dt.add(Calendar.DATE, -3)
                if(dateEqualsToday(dt.time)) return NextDateReturn(d, true)
            }
            "Sa." -> {
                val dt = date
                dt.add(Calendar.DATE, -2)
                if(dateEqualsToday(dt.time)) return NextDateReturn(d, true)
            }
            "So." -> {
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
}