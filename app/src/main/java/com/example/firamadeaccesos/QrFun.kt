package com.example.firamadeaccesos

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlin.math.log

class QrFun {

    private val prom = "Entre a DIGITALINK y escanee el QR generado."

    fun qrIniLec(mainActivity: MainActivity): IntentIntegrator {// Iniciar lectura del QR
        val qrScan = IntentIntegrator(mainActivity)
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrScan.setBeepEnabled(false)
        qrScan.setPrompt(prom)
        qrScan.initiateScan()
        return qrScan
    }
    /* URL ? access_key= 13_digitos Modo*/
    fun qrInf(usrIn: String, con: Context, Dbug: Boolean): Array<String> {//Obtener informacion del QR
        val contDat = arrayOf("", "","") //URL, KEY MODO
        try {
            var stgURL = false
            for (i in usrIn.indices) {
                if (usrIn[i] == '?') {
                    stgURL = true
                } else {
                    if (!stgURL) {
                        contDat[0] = contDat[0] + usrIn[i]//Url
                    } else {
                        if(usrIn[i] == '='){
                            for (x in 1..13){
                                contDat[1] = contDat[1] + usrIn[i+x]//Key
                            }
                            contDat[2] = contDat[2] + usrIn[usrIn.length - 1]//Mod
                        }
                    }
                }
            }
            if (Dbug){
                Log.d("QR","Resibido: $usrIn")
                Log.d("QR", "Url: ${contDat[0]}, Key: ${contDat[1]}, Mod: ${contDat[2]}")
            }
        } catch (e: Exception) {
            Toast.makeText(con, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        return contDat
    }
}
