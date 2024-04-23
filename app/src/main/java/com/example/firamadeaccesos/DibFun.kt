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
    fun dibUn(firma: drawingView, Dbug: Boolean) {
        firma.undo()
        if (Dbug){
            Log.d("Touch cord", "Deshacer")
        }
    }
    fun dibRed(firma: drawingView, Dbug: Boolean) {
        firma.redo()
        if (Dbug){
            Log.d("Touch cord", "Rehacer")
        }
    }
    fun dibCls(firma: drawingView, xCords: MutableList<MutableList<Int>>, ycords: MutableList<MutableList<Int>>, Dbug: Boolean, btnScan: Button) {
        firma.clearDrawingBoard()
        xCords.clear()
        ycords.clear()
        btnScan.isEnabled = false;
        btnScan.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
        if (Dbug){
            if (xCords.isEmpty() && ycords.isEmpty()){
                Log.d("Touch cord", "Limpio")
            }
        }
    }
}