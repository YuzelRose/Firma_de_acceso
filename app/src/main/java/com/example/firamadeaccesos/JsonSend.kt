package com.example.firamadeaccesos


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class JsonSend {
    private val config = Config()
    private val logDbug = LogDbug()
    private lateinit var constURL: String
    private lateinit var tostContext: Context
    private var x = Array(5) { mutableListOf<Int>() }
    private var y = Array(5) { mutableListOf<Int>() }
    private var cntpriv: Int = 0
    fun prosData(contDat: Array<String>, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>, cnt: Int, cont:Context) {
        x[cntpriv].addAll(xCords.flatten())
        y[cntpriv].addAll(yCords.flatten())
        cntpriv++
        logDbug.prosJData(cntpriv,x,y)
        if (cnt == 0) {
            constURL = config.setURL()
            val key = constURL + "?access_key=" + contDat[0]
            logDbug.sendJData(key)
            Toast.makeText(cont,"Procesando datos",Toast.LENGTH_SHORT).show()
            tostContext = cont
            sendJson(key,contDat[0]) {responseJson ->
                showToast(responseJson)
            }
        }
    }
    private fun sendJson(key: String, s: String, callback: (String) -> Unit) {
        val jsonObject = JSONObject().apply {
            put("xCord", JSONArray(x))
            put("yCord", JSONArray(y))
        }
        val jsonBody = jsonObject.toString()
        val mediaType = "application/json; charset=utf-8".toMediaType()

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(key)
            .addHeader("access_key", s)
            .post(jsonBody.toRequestBody(mediaType))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                val fail = e.printStackTrace().toString()
                logDbug.failJSend(fail)
                val responseJson = "Error al conectar al servidor"
                Handler(Looper.getMainLooper()).post {
                    callback(responseJson)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string() ?: ""
                val cod = responseBody.take(4)
                val responseJson = config.responseCode(cod)
                logDbug.responseCodMeaning(responseJson)
                Handler(Looper.getMainLooper()).post {
                    callback(responseJson)
                }
            }
        })
    }
    private fun showToast(responseJson: String) {
        Toast.makeText(tostContext, responseJson, Toast.LENGTH_SHORT).show()
    }
}