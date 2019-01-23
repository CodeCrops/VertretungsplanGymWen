package de.codecrops.vertretungsplangymwen.sqlite

/*
ACHTUNG!!!
    Der Zweck dieser Klasse ist es, alle Informationen über eine abonnierte Klasse/Kurs zu beinhalten,
    damit es möglich ist eine List<PreferencesData> oder ein einfaches Object mit Informationen zu haben, in welcher alle AnzeigeInfos des Kurses vorhanden sind
ACHTUNG!!!
 */

data class PreferencesData(val course: String, val type: PREFERENCETYPE) {

    override fun toString(): String {
        return "$course (${type.id} : ${type.nameinstring})"
    }

}