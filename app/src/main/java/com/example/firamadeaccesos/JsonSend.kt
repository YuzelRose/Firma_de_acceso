package com.example.firamadeaccesos

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

class JsonSend {
    private val config = Config()
    private val logDbug = LogDbug()

    private lateinit var constURL: String
    private var x = Array(5) { mutableListOf<Int>() }
    private var y = Array(5) { mutableListOf<Int>() }
    private var cntpriv: Int = 0
    fun prosData(contDat: Array<String>, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>, cnt: Int) {
        x[cntpriv].addAll(xCords.flatten())
        y[cntpriv].addAll(yCords.flatten())
        cntpriv++
        logDbug.prosJData(cntpriv,x,y)
        if (cnt == 0) {
            constURL = config.setURL()
            val key = constURL + "?access_key=" + contDat[0]
            logDbug.sendJData(key)
            sendJson(key,contDat[0])
        }
    }
    private fun sendJson(key: String, s: String) {

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
                logDbug.failJSend(e.printStackTrace().toString())
            }
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {//Positiva
                val responseData = response.body?.string()
                logDbug.responseJSend(responseData)
            }
        })
    }
}