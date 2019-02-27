package de.codecrops.vertretungsplangymwen.data

import de.codecrops.vertretungsplangymwen.Utils
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author K1TR1K
 * Diese Klasse wandelt den html String in eine in der App nutzbare Form um und stellt
 * nützliche Methoden zur Filterung bereit.
 * @param data Dies ist der String der html Datei
 * @constructor Der Konstruktor wandelt den im Parameter data mitgegebenen String in
 *              eine ArrayList mit Zeilen und Spalten um.
 * @property table Dies ist die Tabelle, die aus dem data String erstellt wurde.
 *           Sie besteht aus einer ArrayList gefüllt mir VertretungData Objekten.
 * @property date Dies ist das Datum, das in der html Datei als aktueller Stand genannt wird.
 */

class Extractor(data: HttpReturnData) {

    var unauthorized = false
    var networkError = false
    lateinit var date: Date
    val table: ArrayList<VertretungData> = arrayListOf()

    init {
        /**
         * Überprüft, ob der ResponseCode 401 ist, was bedeuten würde, dass Nutzername oder
         * Passwort falsch ist.
         */
        when(data.responseCode) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> unauthorized = true
            HttpURLConnection.HTTP_OK -> {
                if(data.data != null) {
                    val thread = Thread {
                        extract(data.data)
                    }
                    thread.start()
                    thread.join()
                } else networkError = true
            }
            else -> networkError = true
        }
    }

    private fun extract(data: String) {
        /*
        Dies erstellt zuerst ein "SimpleDateFormat" mit dem richtigen Format und
        formatiert dann den aus der html Datei geschnittenen String zu einem Date Objekt
         */
        date = SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY).parse(data.substring(
                data.indexOf("</Title>") - 10,
                data.indexOf("</Title>")
        ))

        val c = Calendar.getInstance()
        c.time = date
        val d = Utils.formGermanDate(c)

        //Schneidet die eigentliche Tabelle aus dem html Document String
        val tableString = data.substring(
                data.indexOf("<table class=\"TabelleVertretungen\" cellpadding=\"2px\">"),
                data.lastIndexOf("</table>"))

        //Erstellt eine Liste der Tabellenzeilen
        val lineList = tableString.split("</tr>") as ArrayList<String>

        //Entfernt die erste Zeile der Tabelle, die Definitionszeile
        lineList.removeAt(0)

        //Entfernt die letzte leere Zeile
        lineList.removeAt(lineList.size-1)

        //Diese for-Schleife geht jede einzelne Spalte der Tabellenzeile durch
        for(line: String in lineList) {

            //Erstellt eine Liste der Tabellenspalten
            val colonList = line.split("</td>").toMutableList()
            colonList.removeAt(colonList.size - 1)

            val klasseList: ArrayList<String> = arrayListOf()
            //Setzt die Werte, die für das VertretungData Objekt nötig sind
            val klasse = colonList[0].substring(colonList[0].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "")
            if(klasse.length > 3) {
                val index = klasse.substring(0, 1).toInt()
                when(index) {
                    5 -> { extractMultiKlasse(klasse, klasseList, index) }
                    6 -> { extractMultiKlasse(klasse, klasseList, index) }
                    7 -> { extractMultiKlasse(klasse, klasseList, index) }
                    8 -> { extractMultiKlasse(klasse, klasseList, index) }
                    9 -> { extractMultiKlasse(klasse, klasseList, index) }
                    1 -> {
                        if(klasse.startsWith("10")) {
                            extractMultiKlasse(klasse, klasseList, index)
                        }
                    }
                }
            }
            val stunde = colonList[1].substring(colonList[1].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "").toInt()
            val fach = colonList[2].substring(colonList[2].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "")
            val vertreung = colonList[3].substring(colonList[3].lastIndexOf(">"))
                    .replace(">", "")
            val raum = colonList[4].substring(colonList[4].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "")
            val kommentar = colonList[5].substring(colonList[5].lastIndexOf(">"))
                    .replace(">", "")

            //fügt das VertretungData Objelt der Tabelle hinzu
            if(klasseList.isNotEmpty()) {
                for(k in klasseList) {
                    table.add(VertretungData(k, stunde, vertreung, fach, raum, kommentar))
                }
            } else {
                table.add(VertretungData(klasse, stunde, vertreung, fach, raum, kommentar))
            }
        }
    }

    private fun extractMultiKlasse(klasse: String, klasseList: ArrayList<String>, index: Int) {
        val k = removeNonAlphabetic(klasse)
        val list = k.toCharArray()
        for(c in list) {
            klasseList.add("$index$c")
        }
    }

    private fun removeNonAlphabetic(s: String) : String {
        val r = Regex("[^a-h]")
        s.replace(r, "")
        return s
    }
}