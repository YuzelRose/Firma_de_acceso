package com.example.firamadeaccesos

import android.util.Log
import android.widget.Button
import com.mihir.drawingcanvas.drawingView

class DibFun {

    fun dibini(firma: drawingView) {
        firma.setBrushAlpha(255)
        firma.setBrushColor(R.color.black)
        firma.setSizeForBrush(4)
        firma.erase(R.color.white)
    }
    fun dibUn(firma: drawingView, dbug: Boolean) {
        firma.undo()
        if (dbug){
            Log.d("Touch cord", "Deshacer")
        }
    }
    fun dibRed(firma: drawingView, dbug: Boolean) {
        firma.redo()
        if (dbug){
            Log.d("Touch cord", "Rehacer")
        }
    }
    fun dibCls(firma: drawingView, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>, dbug: Boolean, btnScan: Button) {
        firma.clearDrawingBoard()
        xCords.clear()
        yCords.clear()
        xCords.add(mutableListOf())
        yCords.add(mutableListOf())
        btnScan.isEnabled = false
        btnScan.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
        if (dbug){
            if (xCords.isEmpty() && yCords.isEmpty()){
                Log.d("Touch cord", "Limpio")
            }
        }
    }
}