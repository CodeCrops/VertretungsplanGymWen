package de.codecrops.vertretungsplangymwen.dataextraction

import java.text.DateFormat
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

    val date: Date
    val table: ArrayList<ArrayList<String>> = arrayListOf()

    init {

        /*
        Dies erstellt zuerst ein "SimpleDateFormat" mit dem richtigen Format und
        formatiert dann den aus der html Datei geschnittenen String zu einem Date Objekt
         */
        date = SimpleDateFormat("dd.MM.YYYY", Locale.GERMANY).parse(data.substring(
                data.indexOf("</Title>") - 10,
                data.indexOf("</Title>")
        ))

        //Schneidet die eigentliche Tabelle aus dem html Document String
        val tableString = data.substring(data.indexOf
        ("<table class=\"TabelleVertretungen\" cellpadding=\"2px\">")
        + "<table class=\"TabelleVertretungen\" cellpadding=\"2px\">".length,
                data.lastIndexOf("</table>"))

        //Erstellt eine Liste der Tabellenzeilen
        val lineList = tableString.split("</tr>") as ArrayList<String>

        //Entfernt die letzte unnötige Zeile mit dem einzigen Inhalt: "</tr>"
        lineList.removeAt(lineList.size -1)

        /*
            Der Counter ist wichtig, damit Später die Spalten der Tabelle den
            Zeilen zugeordnet werden können.
             */
        var count = 0

        //Diese for-Schleife geht jede einzelne Zeile der Tabelle durch
        for (line: String in lineList) {

            //Dies ist eine Liste der einzelnen Spalten der Tabelle in der Zeile "line"
            val colonList = line.split("\n") as ArrayList<String>

            //Dies entfernt die html Definition einer Tabellenspalte aus der Spaltenliste
            colonList.remove("</tr>")

            //Dies entfernt die html Definition eines Tabellenanfangs aus der Spaltenliste
            colonList.remove("<tr class=\"TitelZeileTabelleVertretungen\">")

            //Dies fügt der "table" ein Objekt, das als Zeile der Tabelle anzusehen ist hinzu
            table.add(ArrayList())

            /*
            Diese for-Schleife geht jede Spalte der Tabelle in der aktuellen Zeile der
            überliegenden for-Schleife durch.
             */
            for (colon: String in colonList) {

                /*
                Dies schneidet den eigentlichen Wert aus der html Datei in der entsprechenden
                Spalte und Zeile heraus.
                 */
                val value = colon.substring(colon.indexOf(">"), colon.lastIndexOf("<"))

                /*
                Dies fügt der zuvor eingefügen Zeile der "table" die entsprechenden Spalten
                hinzu.
                 */
                table.get(count).add(value)
            }

            /*
            Dies wechselt so zu sagen in die nächste Zeile der Tabelle, dies ist wichtig für
            die for-Schleife, die die Spalten den Zeilen hinzufügt.
             */
            count++
        }
    }
}