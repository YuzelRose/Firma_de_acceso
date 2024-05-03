package com.example.firamadeaccesos

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class JsonSend {
    private val constURL = "https://djackpot.rodokizzzdev.com/digitalink/firmas/Enlace/recibir.php"
    private var x = Array(5) { mutableListOf<Int>() }
    private var y = Array(5) { mutableListOf<Int>() }
    private var cntpriv: Int = 0
    fun prosData(contDat: Array<String>, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>, dbug: Boolean, cnt: Int) {
        x[cntpriv].addAll(xCords.flatten())
        y[cntpriv].addAll(yCords.flatten())
        cntpriv++
        if (dbug) {
            Log.d("JSONSEND","Arreglo numero: $cntpriv")
            Log.d("JSONSEND","x: ${x[cntpriv-1]}")
            Log.d("JSONSEND","y: ${y[cntpriv-1]}")
        }
        if (cnt == 0) {
            val key = constURL + "?access_key=" + contDat[0]
            if (dbug) {
                Log.d("JSONSEND", "Enviando Json a $key")
            }
            sendJson(key,dbug,contDat[0])
        }
    }
    private fun sendJson(key: String, dbug: Boolean, s: String) {

        val jsonObject = JSONObject().apply {//Creacion de un objeto JSON
            put("xCord", JSONArray(x))
            put("yCord", JSONArray(y))
        }
        val jsonBody = jsonObject.toString()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        //Solicitud
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(key)
            .addHeader("access_key", s)
            .post(jsonBody.toRequestBody(mediaType))
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {//Ejecucion y respuestas
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {//Negativa
                if (dbug) {
                    Log.d("JSONSEND", "Error: "+e.printStackTrace())
                }
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {//Positiva
                val responseData = response.body?.string()
                if (dbug) {
                    Log.d("JSONSEND", "Respuesta: $responseData")
                }
            }
        })
    }
}