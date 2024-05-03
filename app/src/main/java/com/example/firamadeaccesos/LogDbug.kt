package com.example.firamadeaccesos

import android.util.Log

class LogDbug {
    // Mod: false: PRODUCTION true: DEVELOPMENT
    private var dbug = true
    fun error(place: String, e: String?) {
        if (dbug){
            Log.d("Error", "$place: Error: $e")
            Log.d("Pross", "$place: Error: $e")

        }
    }
    fun touchMSG(touch: Int, touchMSG: String) {
        if (dbug) {
            Log.d("Touch", "Touch: $touch$touchMSG")
            Log.d("Pross", "Touch: $touch$touchMSG")
        }
    }

    fun touchEnd(touch: Int) {
        if (dbug) {
            Log.d("Touch", "Fin de la pulsacion siguiente posicion: $touch")
            Log.d("Pross", "Fin de la pulsacion siguiente posicion: $touch")
        }
    }

    fun touchClear(xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>) {
        if (dbug) {
            if (xCords.isEmpty() && yCords.isEmpty()) {
                Log.d("Touch", "Limpio")
                Log.d("Pross", "Limpio")
            }
        }
    }

    fun touchAction(action: String) {
        if (dbug) {
            Log.d("Touch", action)
            Log.d("Pross", action)
        }
    }

    fun cordsAsGet(x: Int, y: Int) {
        if (dbug) {
            Log.d("Cordenadas", "Coordenadas: X: $x, Y: $y")
            Log.d("Pross", "Coordenadas: X: $x, Y: $y")
        }
    }

    fun qrGet(usrIn: String, contDat: Array<String>) {
        if (dbug) {
            Log.d("QR", "Resibido: $usrIn")
            Log.d("QR", "Key: ${contDat[0]}, Mod: ${contDat[1]}")
            Log.d("Pross", "Resibido: $usrIn")
            Log.d("Pross", "Key: ${contDat[0]}, Mod: ${contDat[1]}")
        }
    }

    fun prosJData(cntpriv: Int, x: Array<MutableList<Int>>, y: Array<MutableList<Int>>) {
        if (dbug) {
            Log.d("JSONSEND","Arreglo numero: $cntpriv")
            Log.d("JSONSEND","x: ${x[cntpriv-1]}")
            Log.d("JSONSEND","y: ${y[cntpriv-1]}")
            Log.d("Pross","Arreglo numero: $cntpriv")
            Log.d("Pross","x: ${x[cntpriv-1]}")
            Log.d("Pross","y: ${y[cntpriv-1]}")
        }
    }

    fun sendJData(key: String) {
        if (dbug) {
            Log.d("JSONSEND", "Enviando Json a $key")
        }
    }

    fun failJSend(e: String) {
        if (dbug) {
            Log.d("JSONSEND", "Fallo al enviar JSON: $e")
        }
    }

    fun responseJSend(responseData: String?) {
        if (dbug) {
            Log.d("JSONSEND", "Respuesta: $responseData")
        }
    }
}