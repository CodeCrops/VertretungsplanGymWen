package de.codecrops.vertretungsplangymwen.data

/**
 * @param kuerzel Das Kürzel der Lehrkraft
 * @param nachname Der Nachname der Lehrkraft
 * @param vorname Der Vorname der Lehrkraft (nullable, falls nicht bekannt)
 * @param geschlecht Das Geschlecht der Lehrkraft
 */

/*
ACHTUNG!!!
    Der Zweck dieser Klasse ist es, alle Informationen über eine Lehrkraft zu beinhalten,
    damit es möglich ist eine List<LehrerData> oder ein einfaches Object mit Informationen zu haben, in welcher alle AnzeigeInfos der Lehrkraft vorhanden sind
ACHTUNG!!!
 */

data class LehrerData(val kuerzel: String, val nachname: String, val vorname: String?, val geschlecht: String) {

    override fun toString(): String {
        return "$kuerzel: $nachname $vorname ($geschlecht)"
    }
}