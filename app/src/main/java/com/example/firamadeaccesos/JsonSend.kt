package com.example.firamadeaccesos

import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class JsonSend {
    private var constURL = "https://djackpot.rodokizzzdev.com/digitalink/firmas/Enlace/recibir.php"
    fun prosData(contDat: Array<String>, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>,dbug: Boolean) {
        val key = contDat[0]
        val xCord: List<Int> = xCords.flatten()
        val yCord: List<Int> = yCords.flatten()
        sendJson(key,xCord,yCord,dbug)
    }
    private fun sendJson(key:String, x:List<Int>, y:List<Int>, dbug: Boolean) {

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