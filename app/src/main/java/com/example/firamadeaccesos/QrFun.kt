@file:Suppress("DEPRECATION")

package com.example.firamadeaccesos
import android.content.Context
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

class QrFun {
    private val config = Config()
    private val logDbug = LogDbug()

    fun qrIniLec(mainActivity: MainActivity): IntentIntegrator {// Iniciar lectura del QR
        val qrScan = IntentIntegrator(mainActivity)
        qrScan.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        qrScan.setBeepEnabled(false)
        qrScan.setPrompt(config.setQRProm())
        qrScan.initiateScan()
        return qrScan
    }
    fun qrInf(usrIn: String, con: Context): Array<String> {//Obtener informacion del QR
        val contDat = arrayOf("", "") //KEY, MOD
        try {
            for (x in 0 until 13) {
                contDat[0] = contDat[0] + usrIn[x]//Key
            }
            contDat[1] = contDat[1] + usrIn[usrIn.length - 1]//Mod
            logDbug.qrGet(usrIn,contDat)
        } catch (e: Exception) {
            Toast.makeText(con, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        return contDat
    }
}