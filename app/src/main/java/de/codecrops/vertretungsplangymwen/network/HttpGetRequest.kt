package de.codecrops.vertretungsplangymwen.network

/**
 * @author K1TR1K
 */

import android.content.Context
import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import de.codecrops.vertretungsplangymwen.credentials.CredentialsManager
import de.codecrops.vertretungsplangymwen.data.Extractor
import de.codecrops.vertretungsplangymwen.data.HttpReturnData

/**
 * @author K1TR1K
 * Diese Klasse bietet als AsyncTask eine asynchrone Methode um die html Datei der mitgegebenen URL
 * als String zurückzugeben.
 * @property autKey der Authentication Key, erstellt aus Nutzername und Passwort für den Download der Datei von der Website
 */

class HttpGetRequest : AsyncTask<String, Void, HttpReturnData>() {
    private var autKey: String = ""
    private var passwordCheck: Boolean = false

    companion object {
        /**
         * Gibt den Vertretungsplan für den heutigen Tag zurück.
         * @return Extractor Objekt mit den Daten des heutigen Tages
         */
        fun extractToday(context: Context): Extractor {
            val getRequest = HttpGetRequest()
            getRequest.passwordCheck = false
            getRequest.autKey = CredentialsManager.convertToBase64(context)
            return Extractor(getRequest.execute("http://gym-wen.de/vp/heute.htm").get())
        }

        /**
         * Gibt den Vertretungsplan für den morgigen Tag zurück.
         * @return Extractor Objekt mit den Daten des morgigen Tages
         */
        fun extractTomorrow(context: Context): Extractor {
            val getRequest = HttpGetRequest()
            getRequest.passwordCheck = false
            getRequest.autKey = CredentialsManager.convertToBase64(context)
            return Extractor(getRequest.execute("http://gym-wen.de/vp/morgen.htm").get())
        }

        fun getResponseCodeForPasswordCheck(usernamePassword: String): Int {
            val getRequest = HttpGetRequest()
            getRequest.passwordCheck = true
            getRequest.autKey = Base64.encodeToString(usernamePassword.toByteArray(), 0).replace("\n", "")
            return getRequest.execute("http://gym-wen.de/vp/heute.htm").get().responseCode
        }
    }

    /**
     * Diese Methode initialisiert den Download der Datei und gibt den fertigen String zurück
     * @param params Ein String Array, das als erstes Element die Ziel-URL enthalten muss
     * @return der String der html Datei
     */
    override fun doInBackground(vararg params: String?): HttpReturnData {
        val url = createUrl(params[0]!!)
        lateinit var response: HttpReturnData
        try {
            response = makeHttpRequest(url!!)
        } catch (e: IOException) {
        }
        return response
    }

    /**
     * Erstellt eine URL aus dem URL-Adressen-String
     */
    private fun createUrl(stringUrl: String): URL? {
        try {
            return URL(stringUrl)
        } catch (e: MalformedURLException) {
        }
        return null
    }

    /**
     * Initialisiert die eigentliche URL Verbindung, liest die html Datei aus und schließt die
     * Verbindung wieder.
     * @param url Die URL der zu herunterladenden html Datei
     * @return Der String der html Datei
     */
    private fun makeHttpRequest(url: URL): HttpReturnData  {
        var dataResponse: String? = null
        var responseCodeResponse = 0
        lateinit var  urlConnection: HttpURLConnection
        var inputStream: InputStream? = null

        try {
            urlConnection = url.openConnection() as HttpURLConnection

            //Setzt den Authentication Key für die Verbindung
            urlConnection.setRequestProperty("Authorization", "Basic $autKey")
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 20000
            urlConnection.connectTimeout = 20000
            urlConnection.connect()

            if(urlConnection.responseCode == HttpURLConnection.HTTP_OK && !passwordCheck) {
                inputStream = urlConnection.inputStream
                dataResponse = readFromStream(inputStream)
            }

            responseCodeResponse = urlConnection.responseCode
        } catch (e: IOException) {
        } finally {
            urlConnection.disconnect()
            inputStream?.close()
        }
        return HttpReturnData(responseCodeResponse, dataResponse)
    }

    private fun readFromStream(inputStream: InputStream): String {
        val output = StringBuilder()
        val inputStreamReader = InputStreamReader(inputStream, "WINDOWS-1252")
        val reader = BufferedReader(inputStreamReader)
        var line = reader.readLine()
        while (line != null) {
            output.append(line)
            line = reader.readLine()
        }
        return output.toString()
    }

    override fun onPostExecute(result: HttpReturnData) {
        super.onPostExecute(result)
    }
}
