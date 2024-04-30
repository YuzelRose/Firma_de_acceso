package com.example.firamadeaccesos

import android.util.Log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class JsonFun {
    private var constURL = "https://djackpot.rodokizzzdev.com/digitalink/firmas/Enlace/recibir.php"
    fun prosData(contDat: Array<String>, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>,dbug: Boolean) {
        val key = contDat[0]
        val xCord: List<Int> = xCords.flatten()
        val yCord: List<Int> = yCords.flatten()
        sendJson(key,constURL,xCord,yCord,dbug)
    }
    data class VerifDJson(val accesKey: String, val xCordList: List<Int>, val yCordList: List<Int>)


    private fun sendJson(key:String, constURL:String, x:List<Int>, y:List<Int>, dbug: Boolean){
        if (dbug){
            Log.d("JSON", "Cargando")
        }
        val data = VerifDJson(key,x,y)
        val jsonString = Json.encodeToString(data)

        val url = URL(constURL)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8")
        connection.doOutput = true

        val outputStreamWriter = connection.outputStream.bufferedWriter()
        outputStreamWriter.write(jsonString)
        outputStreamWriter.flush()
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            if (dbug){
                Log.d("Response", "Response: $response")
            }
        } else {
            if (dbug){
                Log.d("Response", "Error sending data: $responseCode")
            }
        }
    }
}
