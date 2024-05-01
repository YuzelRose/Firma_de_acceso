package com.example.firamadeaccesos

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class JsonSend {
    private val constURL = "https://djackpot.rodokizzzdev.com/digitalink/firmas/Enlace/recibir.php"
    private var x = mutableListOf<MutableList<Int>>()
    private var y = mutableListOf<MutableList<Int>>()
    private var cntpriv: Int = 0
    fun prosData(contDat: Array<String>, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>, dbug: Boolean, cnt: Int) {
        val key = contDat[0]
        val xCord: List<Int> = xCords.flatten()
        val yCord: List<Int> = yCords.flatten()
        x.add(xCord.toMutableList())
        y.add(yCord.toMutableList())
        if (dbug) {
            Log.d("JSONSEND","Arreglo numero: ${cntpriv+1}")
            Log.d("JSONSEND","x: ${x[cntpriv]}")
            Log.d("JSONSEND","y: ${y[cntpriv]}")
        }
        cntpriv++
        if (cnt == 0) {
            if (dbug) {
                Log.d("JSONSEND", "Enviando Json a $constURL")
                Log.d("JSONSEND", "Key: $key")
            }
            sendJson(key,dbug)
            cntpriv = 0
        }
    }
    private fun sendJson(key:String, dbug: Boolean) {

        val jsonObject = JSONObject().apply {//Creacion de un objeto JSON
            put("key", key)
            put("xCord", x)
            put("yCord", y)
        }
        val jsonBody = jsonObject.toString()
        val mediaType = "application/json; charset=utf-8".toMediaType()
        //Solicitud
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(constURL)
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