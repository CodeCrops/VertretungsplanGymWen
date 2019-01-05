package de.codecrops.vertretungsplangymwen.data

import de.codecrops.vertretungsplangymwen.network.HttpGetRequest

/**
 * Diese Klasse bietet jeweils eine Methode um für den heutigen und morgigen Tag die Daten des
 * Vertretungsplans herunterzuladen und mithilfe der Extractor Klasse zu extrahieren.
 */
object DataPull {

    /**
     * Gibt den Vertretungsplan für den heutigen Tag zurück.
     * @return Extractor Objekt mit den Daten des heutigen Tages
     */
    fun extractToday(): Extractor {
        val getRequest = HttpGetRequest()
        return Extractor(getRequest.execute("http://gym-wen.de/vp/heute.htm").get())
    }

    /**
     * Gibt den Vertretungsplan für den morgigen Tag zurück.
     * @return Extractor Objekt mit den Daten des morgigen Tages
     */
    fun extractTomorrow(): Extractor {
        val getRequest = HttpGetRequest()
        return Extractor(getRequest.execute("http://gym-wen.de/vp/morgen.htm").get())
    }
}