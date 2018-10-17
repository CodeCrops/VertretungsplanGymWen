package de.codecrops.vertretungsplangymwen.network

/**
 * Diese Klasse bietet jeweils eine Methode, um die html Datei des heutigen oder morigen Tages als
 * String zu bekommen.
 */
object HttpPull {

    //Gibt die Datei des aktuellen Tages zurück
    fun getToday(): String {
        val getRequest = HttpGetRequest()
        return getRequest.execute("http://gym-wen.de/vp/heute.htm").get()
    }

    //Gibt die Datei des morigen Tages zurück
    fun getTomorrow(): String {
        val getRequest = HttpGetRequest()
        return getRequest.execute("http://gym-wen.de/vp/morgen.htm").get()
    }
}