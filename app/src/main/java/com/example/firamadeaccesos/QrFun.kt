package com.example.firamadeaccesos

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

class QrFun {

    private val prom = "Entre a la pagina y escanie el QR generado con su usuario"

    fun qrIniLec(mainActivity: MainActivity): IntentIntegrator {// Iniciar lectura del QR
        val qrScan = IntentIntegrator(mainActivity)
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrScan.setBeepEnabled(false)
        qrScan.setPrompt(prom)
        qrScan.initiateScan()
        return qrScan
    }

    fun qrInf(usrIn: String, con: Context, Dbug: Boolean): Array<String> {//Obtener informacion del QR
        val contDat = arrayOf("", "")
        try {
            var usred = false
            for (i in usrIn.indices) {
                if (usrIn[i] == '_') {
                    usred = true
                } else {
                    if (!usred) {
                        contDat[0] = contDat[0] + usrIn[i]//Usuario
                    } else {
                        contDat[1] = contDat[1] + usrIn[i]//Url
                    }
                }
            }
            if (Dbug){
                Log.d("Datos del QR", "Usuario: ${contDat[0]}, URL: ${contDat[1]}")
            }
        } catch (e: Exception) {
            Toast.makeText(con, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        return contDat
    }
}
