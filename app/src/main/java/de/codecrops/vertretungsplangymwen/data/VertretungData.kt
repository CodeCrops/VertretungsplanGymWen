package de.codecrops.vertretungsplangymwen.data

/**
 * Diese Klasse ist das Datenobjekt der Vertretungen mit allen möglichen Variablem
 * @param klasse Bezeichnung der Klasse oder des Kurses
 * @param stunde Schulstunde
 * @param vertretung vertretende Lehrkraft / Entfallen der Stunde
 * @param fach vertretenes Fach
 * @param raum Raum
 * @param kommentar Auf der Website genannte Spalte "sonstiges"
 */

data class VertretungData(val klasse: String,
                          val stunde: Int,
                          val vertretung: String,
                          val fach: String,
                          val raum: String,
                          val kommentar: String) {

    /**
     * @return gibt eine standartmäßig ausformulierte Form der VertretungData aus
     */
    override fun toString(): String {
        return "$klasse $stunde Std. $vertretung Fach: $fach Raum: $raum; $kommentar"
    }
}