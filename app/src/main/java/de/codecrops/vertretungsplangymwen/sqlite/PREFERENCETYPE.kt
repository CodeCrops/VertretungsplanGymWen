package de.codecrops.vertretungsplangymwen.sqlite

enum class PREFERENCETYPE(val id: Int, val nameinstring: String) {
    REGULÄR(1, "Regulärer Kurs"),
    WAHLKURS(2, "Wahlkurs"),
    ANDERS(3, "Anders");

    companion object {
        fun byID(id : Int) : PREFERENCETYPE {
            for(type in PREFERENCETYPE.values()) {
                when(type.id) {
                    REGULÄR.id -> return REGULÄR
                    WAHLKURS.id -> return WAHLKURS
                }
            }
            return ANDERS
        }
    }

}