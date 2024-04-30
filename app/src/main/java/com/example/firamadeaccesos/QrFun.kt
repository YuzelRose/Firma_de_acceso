@file:Suppress("DEPRECATION")

package com.example.firamadeaccesos
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

class QrFun {

    private val prom = "Entré a DIGITALINK y escaneé el QR generado."

    fun qrIniLec(mainActivity: MainActivity): IntentIntegrator {// Iniciar lectura del QR
        val qrScan = IntentIntegrator(mainActivity)
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrScan.setBeepEnabled(false)
        qrScan.setPrompt(prom)
        qrScan.initiateScan()
        return qrScan
    }

    /* URL ? access_key= 13_digitos Modo*/
    fun qrInf(usrIn: String, con: Context, dbug: Boolean): Array<String> {//Obtener informacion del QR
        val contDat = arrayOf("", "") //KEY, MOD
        try {
            for (x in 0 until 13) {
                contDat[0] = contDat[0] + usrIn[x]//Key
            }
            contDat[1] = contDat[1] + usrIn[usrIn.length - 1]//Mod
            if (dbug) {
                Log.d("QR", "Resibido: $usrIn")
                Log.d("QR", "Key: ${contDat[0]}, Mod: ${contDat[1]}")
            }
        } catch (e: Exception) {
            Toast.makeText(con, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        return contDat
    }
}