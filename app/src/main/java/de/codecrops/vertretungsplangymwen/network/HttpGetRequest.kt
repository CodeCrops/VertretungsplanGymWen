package de.codecrops.vertretungsplangymwen.network

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import android.util.Base64

/**
 * Diese Klasse bietet als AsyncTask eine asynchrone Methode um die html Datei der mitgegebenen URL
 * als String zurückzugeben.
 */

class HttpGetRequest : AsyncTask<String, Void, String>() {

    /**
     * Diese Methode initialisiert den Download der Datei und gibt den fertigen String zurück
     * @param params Ein String Array, das als erstes Element die Ziel-URL enthalten muss
     * @return der String der html Datei
     */
    override fun doInBackground(vararg params: String?): String {
        val url = createUrl(params[0]!!)
        var response = ""
        try {
            response = makeHttpRequest(url!!)
        } catch (e: IOException) {
        }
        return response
    }

    //Erstellt eine URL aus dem URL-Adressen-String
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
    private fun makeHttpRequest(url: URL): String  {
        var response = ""
        lateinit var  urlConnection: HttpURLConnection
        lateinit var inputStream: InputStream

        //Username:Password TODO: automatisches Setzen der Werte durch Anmeldung
        val userpasswort = ""

        //erstellt einen Authentication key aus Nutzername und Passwort mithilfe von Base64
        val encAutorization = Base64.encodeToString(userpasswort.toByteArray(), 0)
                .replace("\n", "")

        try {
            urlConnection = url.openConnection() as HttpURLConnection

            //Setzt den Authentication Key für die Verbindung
            urlConnection.setRequestProperty("Authorization", "Basic $encAutorization")
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 20000
            urlConnection.connectTimeout = 20000
            urlConnection.connect()

            if(urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.inputStream
                response = readFromStream(inputStream)
            } else
                //Gibt den Fehlercode 401 zurück, wenn Nutzername oder Passwort falsch sind
                if(urlConnection.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return "401"
            }
        } catch (e: IOException) {

        } finally {
            urlConnection.disconnect()
            inputStream.close()
        }
        return response
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

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
    }
}
