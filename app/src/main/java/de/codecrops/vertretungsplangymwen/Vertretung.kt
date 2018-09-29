package de.codecrops.vertretungsplangymwen

/**
 * Diese Klasse ist das Datenobjekt der Vertretungen mit allen möglichen Variablem
 * @param schoolClass Bezeichnung der Klasse oder des Kurses
 * @param hour Schulstunde
 * @param representant vertretende Lehrkraft / Entfallen der Stunde
 * @param subject vertretenes Fach
 * @param room Raum
 * @param comment Auf der Website genannte Spalte "sonstiges"
 */

data class Vertretung(val schoolClass: String,
                      val hour: Int,
                      val representant: String,
                      val subject: String,
                      val room: String,
                      val comment: String) {

    /**
     * @return gibt eine standartmäßig ausformulierte Form der Vertretung aus
     */
    override fun toString(): String {
        return "$schoolClass $hour Std. $representant Fach: $subject Raum: $room; $comment"
    }
}