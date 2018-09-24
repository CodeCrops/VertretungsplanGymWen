package de.codecrops.vertretungsplangymwen.network

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpGetRequest : AsyncTask<String, Void, String>() {
    private val requestMethod = "GET"
    private val readTimeout = 15000
    private val connectionTimeout = 15000

    override fun doInBackground(vararg params: String?): String {
        val stringUrl = params[0]

        try {
            val Url = URL(stringUrl)
            val connection = Url.openConnection() as HttpURLConnection

            connection.requestMethod = requestMethod
            connection.readTimeout = readTimeout
            connection.connectTimeout = connectionTimeout

            connection.connect()

            val streamReader = InputStreamReader(connection.inputStream)
            val reader = BufferedReader(streamReader)
            val sb = StringBuilder()

            if (reader.readLine() != null) {
                sb.append(reader.readLine())
            }

            reader.close()
            streamReader.close()

            return sb.toString()
        } catch (e: IOException) {
            e.printStackTrace();
        }

        return "Fail"
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
    }
}