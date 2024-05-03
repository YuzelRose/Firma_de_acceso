package com.example.firamadeaccesos

import android.widget.Button
import com.mihir.drawingcanvas.drawingView

class DibFun {
    private val logDbug = LogDbug()
    fun dibini(firma: drawingView) {
        firma.setBrushAlpha(255)
        firma.setBrushColor(R.color.black)
        firma.setSizeForBrush(4)
        firma.erase(R.color.white)
    }
    fun dibUn(firma: drawingView) {
        firma.undo()
        logDbug.touchAction("Deshacer")
    }
    fun dibRed(firma: drawingView) {
        firma.redo()
        logDbug.touchAction("Rehacer")
    }
    fun dibCls(firma: drawingView, xCords: MutableList<MutableList<Int>>, yCords: MutableList<MutableList<Int>>, btnScan: Button) {
        firma.clearDrawingBoard()
        xCords.clear()
        yCords.clear()
        xCords.add(mutableListOf())
        yCords.add(mutableListOf())
        btnScan.isEnabled = false
        btnScan.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
        logDbug.touchClear(xCords,yCords)
    }
}