package com.example.firamadeaccesos

import android.app.Activity
import android.content.Context
import android.content.Intent

class Config {
    private val logDbug = LogDbug()
    // URL SEND
    fun setURL(): String {
        return "https://djackpot.rodokizzzdev.com/digitalink/firmas/Enlace/recibir.php"
    }
    // Prom QR
    fun setQRProm(): String {
        return "Entré a DIGITALINK y escaneé el QR generado."
    }
    fun reset(contexto: Context) {
        val intent = Intent(contexto, contexto::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        contexto.startActivity(intent)
        (contexto as Activity).finish()
    }
    // Result list
    fun responseCode(responseData: String) {
        logDbug.responseCod(responseData)
        val resStg: String = when (responseData) {
            "8765" -> {
                "Datos recibidos correctamente"
            }

            "5555" -> {
                "Código QR caducado"
            }

            "6543" -> {
                "Fallo en el envío"
            }

            else -> {
                "Error desconocido"
            }
        }
        logDbug.responseCodMeaning(resStg)
    }
}