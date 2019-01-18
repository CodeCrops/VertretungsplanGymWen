package de.codecrops.vertretungsplangymwen.data

import de.codecrops.vertretungsplangymwen.sqlite.DBManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Diese Klasse wandelt den html String in eine in der App nutzbare Form um und stellt
 * nützliche Methoden zur Filterung bereit.
 * @param data Dies ist der String der html Datei
 * @constructor Der Konstruktor wandelt den im Parameter data mitgegebenen String in
 *              eine ArrayList mit Zeilen und Spalten um.
 * @property table Dies ist die Tabelle, die aus dem data String erstellt wurde.
 *           Sie besteht aus einer ArrayList, die als Zeilen der Tabelle anzusehen ist und
 *           einer weiteren ArrayList in jedem ersten Array, das als Spalten anzusehen ist.
 * @property date Dies ist das Datum, das in der html Datei als aktueller Stand genannt wird.
 */

class Extractor(data: String) {

    var unauthorized = false
    lateinit var date: Date
    val table: ArrayList<VertretungData> = arrayListOf()

    init {
        /**
         * Überprüft, ob der ResponseCode 401 ist, was bedeuten würde, dass Nutzername oder
         * Passwort falsch ist.
         */
        if(data.equals("401")) {
            unauthorized = true
        } else {
            extract(data)
        }
    }

    private fun extract(data: String) {
        /*
        Dies erstellt zuerst ein "SimpleDateFormat" mit dem richtigen Format und
        formatiert dann den aus der html Datei geschnittenen String zu einem Date Objekt
         */
        date = SimpleDateFormat("dd.MM.YYYY", Locale.GERMANY).parse(data.substring(
                data.indexOf("</Title>") - 10,
                data.indexOf("</Title>")
        ))

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

            //Setzt die Werte, die für das VertretungData Objekt nötig sind
            val klasse = colonList[0].substring(colonList[0].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "")
            val stunde = colonList[1].substring(colonList[1].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "").toInt()
            val vertreung = colonList[2].substring(colonList[2].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "")
            val fach = colonList[3].substring(colonList[3].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "")
            val raum = colonList[4].substring(colonList[4].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "")
            val kommentar = colonList[5].substring(colonList[5].lastIndexOf(">"))
                    .replace(">", "")
                    .replace(" ", "")

            //fügt das VertretungData Objelt der Tabelle hinzu
            table.add(VertretungData(klasse, stunde, vertreung, fach, raum, kommentar))
        }
    }
}