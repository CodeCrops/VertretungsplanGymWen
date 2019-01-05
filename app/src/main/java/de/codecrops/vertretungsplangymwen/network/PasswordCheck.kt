package de.codecrops.vertretungsplangymwen.network

import android.os.AsyncTask
import android.util.Base64
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class PasswordCheck : AsyncTask<String, Void, Boolean>() {

    companion object {
        fun isCorrect(usernamepassword: String) : Boolean {
            val up = usernamepassword
            return PasswordCheck().execute(usernamepassword).get()
        }
    }

    override fun doInBackground(vararg params: String?): Boolean {
        val url = createUrl("http://gym-wen.de/vp/heute.htm")
        var result = false
        try {
            result = makeHttpRequest(url!!, params[0]!!)
        } catch (e: IOException) {
        }
        return result
    }

    //Erstellt eine URL aus dem URL-Adressen-String
    private fun createUrl(stringUrl: String): URL? {
        try {
            return URL(stringUrl)
        } catch (e: MalformedURLException) {
        }
        return null
    }

    private fun makeHttpRequest(url: URL, pw: String): Boolean  {
        lateinit var  urlConnection: HttpURLConnection
        var unauthorized = false

        //erstellt einen Authentication key aus Nutzername und Passwort mithilfe von Base64
        val encAutorization = Base64.encodeToString(pw.toByteArray(), 0)
                .replace("\n", "")

        try {
            urlConnection = url.openConnection() as HttpURLConnection

            //Setzt den Authentication Key für die Verbindung
            urlConnection.setRequestProperty("Authorization", "Basic $encAutorization")
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 20000
            urlConnection.connectTimeout = 20000
            urlConnection.connect()

            val lol = urlConnection.responseCode

            if(urlConnection.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                unauthorized = true
            }
        } catch (e: IOException) {

        } finally {
            urlConnection.disconnect()
        }
        return !unauthorized
    }

    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
    }
}