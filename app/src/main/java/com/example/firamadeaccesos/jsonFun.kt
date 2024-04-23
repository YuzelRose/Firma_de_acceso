package com.example.firamadeaccesos

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import kotlinx.serialization.*
import java.net.URL

class jsonFun {
    fun ProsData(contDat: Array<String>, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>) {
        val USR = contDat[0]
        val URL = contDat[1]
    }

    data class VerifDJson(val USR: String, val Xcord: List<Int>, val Ycord: List<Int>)

    fun SendJson(context: Context,USR:String,URL:String,x:List<Int>,y:List<Int>,Dbug: Boolean){
        if (Dbug){
            Log.d("JSON", "Cargando")
        }
        val data = VerifDJson(USR,x,y)
        val jsonString = Json.encodeToString(data)

        val url = URL(URL)
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
            Toast.makeText(context,"Datos enviados puede cerrar la aplicacion",Toast.LENGTH_SHORT).show()
            if (Dbug){
                Log.d("Response", "Response: $response")
            }
        } else {
            if (Dbug){
                Log.d("Response", "Error sending data: $responseCode")
            }
        }
    }
}