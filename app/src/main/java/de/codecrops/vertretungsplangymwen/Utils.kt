package de.codecrops.vertretungsplangymwen

import android.text.Html
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.text.Spanned

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


}