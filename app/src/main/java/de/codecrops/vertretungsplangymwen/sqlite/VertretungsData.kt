package de.codecrops.vertretungsplangymwen.sqlite

/**
 * @param klasse Die Klasse, welche die Vertretung betrifft (in Kursen ist dies die Kursnummer)
 * @param stunde Die Stunde, in welcher die Vertretung stattfindet
 * @param vertretung Die Lehrkraft, welche die Vertretung durchführt (evtl. auch hier "entfällt XYZ")
 * @param fach Das Fach, welches anstelle des ursprünglichen Fachs unterrichtet wird (nur nötig, wenn es entfällt)
 * @param raum Der Raum, in welchem anstelle des ursprünglichen Raums unterrichtet wird (nicht nötig, wenn es entfällt)
 * @param sonstiges Sonstige Informationen
 */

/*
ACHTUNG!!!
    Der Zweck dieser Klasse ist es, alle Informationen über eine Vertretung zu beinhalten,
    damit es möglich ist eine List<VertretungsplanData> zu haben, in welcher alle Vertretungen eines Schülers enthalten sind
ACHTUNG!!!
 */

class VertretungsData(val klasse: String, val  stunde: Int, val  vertretung: String, val   fach: String?, val   raum: String?, val   sonstiges: String?) {
}