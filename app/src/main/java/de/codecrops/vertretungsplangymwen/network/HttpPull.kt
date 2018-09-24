package de.codecrops.vertretungsplangymwen.network

object HttpPull {
    fun getToday(): String {
        val getRequest = HttpGetRequest()
        return getRequest.execute("http://gym-wen.de/vp/heute.htm").get()
    }

    fun getTomorrow(): String {
        val getRequest = HttpGetRequest()
        return getRequest.execute("http://gym-wen.de/vp/morgen.htm").get()
    }

    //lel 
}