package com.example.firamadeaccesos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.integration.android.IntentIntegrator
import com.mihir.drawingcanvas.drawingView;

class MainActivity : AppCompatActivity() {

    // Mostrar los datos escaneados
    private var Dbug = true
    // Botones activados
    private var BRed = true
    private var BUnd = true

    private var Touch = 0

    private lateinit var btnScan: Button
    private lateinit var btnUn: Button
    private lateinit var btnRed: Button
    private lateinit var btnCls: Button
    private lateinit var firma: drawingView

    private val qrFun = QrFun()
    private val dibFun = DibFun()
    private val jsonFun = jsonFun()

    private val xCords = mutableListOf<MutableList<Int>>()
    private val yCords = mutableListOf<MutableList<Int>>()

    private val xCordsGet = mutableListOf<Int>()
    private val yCordsGet = mutableListOf<Int>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Firma
        firma = findViewById(R.id.drawing_view)
        dibFun.dibini(firma)//Inicializar los parametros del drawingView
        //Lector de QRs
        btnScan = findViewById(R.id.btnEscanear)
        btnScan.setOnClickListener { qrFun.qrIniLec(this) }
        //limpiar
        btnCls = findViewById(R.id.btnCls)
        btnCls.setOnClickListener {
            dibFun.dibCls(firma,xCords,yCords,Dbug,btnScan)
            Touch = 0
            BUnd = false
            BRed = false
            btnUn.isEnabled = false
            btnRed.isEnabled = false
            btnScan.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
            if (Dbug) {
                Log.d("Touch cord", "Touch: $Touch")

            }
        }
        //Deshacer
        btnUn = findViewById(R.id.btnUn)
        btnUn.setOnClickListener {
            if (BUnd) {
                dibFun.dibUn(firma,Dbug)
                Touch--
                if (Touch>=0){
                    btnScan.isEnabled = false;
                    btnScan.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
                }
                BUnd = false
                BRed = true
                btnUn.isEnabled = false
                btnRed.isEnabled = true
                if (Dbug) {
                    Log.d("Touch cord", "Touch: $Touch")

                }
            }
        }
        //Rehacer
        btnRed = findViewById(R.id.btnRed)
        btnRed.setOnClickListener {
            if (BRed) {
                dibFun.dibRed(firma,Dbug)
                Touch++
                BRed = false
                BUnd = true
                btnRed.isEnabled = false
                btnUn.isEnabled = true
                if (Dbug) {
                    Log.d("Touch cord", "Touch: $Touch")

                }
            }
        }

        firma.setOnTouchListener { view, event ->//
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("Touch cord", "Pulsacion")
                    BRed = false
                    BUnd = true
                    btnRed.isEnabled = false
                    btnUn.isEnabled = true
                    if (xCords.getOrNull(Touch)?.isNotEmpty() == true) {
                        xCords.removeAt(Touch)
                        yCords.removeAt(Touch)
                        if (Dbug) {
                            Log.d("Touch cord", "Datos sobreescritos")
                        }
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d("Touch cord", "Arrastre")
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    if (x >= 0 && x <= firma.width && y >= 0 && y <= firma.height) {
                        xCordsGet.add(x)
                        yCordsGet.add(y)
                        if (Dbug) {
                            Log.d("Touch cord", "Coordenadas: X: $x, Y: $y")
                        }
                        btnCls.isEnabled = true
                        btnUn.isEnabled = true
                        btnRed.isEnabled = true
                        btnScan.isEnabled = true
                        btnScan.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFA5A0A0.toInt())
                    }
                }
                MotionEvent.ACTION_UP -> {
                    xCords.add(xCordsGet)
                    yCords.add(yCordsGet)
                    Touch++
                    if (Dbug) {
                        Log.d("Touch cord", "Fin de la pulsacion siguiente posicion: $Touch")
                    }
                }
            }
            view.performClick()
        }

        findViewById<View>(R.id.main)?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {//Resultado del scaner
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            result?.let {
                if (it.contents.isNullOrEmpty()) {
                    Toast.makeText(this, "Qr no valido", Toast.LENGTH_SHORT).show()
                } else {
                    if (Dbug){// Se muestran las cordenadas guardadas
                        for ((index, filaX) in xCords.withIndex()) {
                            val filaY = yCords[index]
                            for (i in filaX.indices) {
                                Log.d("Cordenadas", "x: ${filaX[i]} y: ${filaY[i]}")
                            }
                        }
                    }
                    val usrIn = it.contents
                    Toast.makeText(this, "Espere un poco", Toast.LENGTH_SHORT).show()
                    val contDat = qrFun.qrInf(usrIn, this,Dbug)//Obtencion de los datos del QR
                    jsonFun.ProsData(contDat,xCords,yCords,Dbug)// Procesamiento de la informacion para el envio
                    Toast.makeText(this,"Datos enviados ",Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Lectura incorrecta", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            if (Dbug){
                Log.d("Error ActivityResult", "Error: ${e.message}")
            }
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        finally {
            dibFun.dibCls(firma,xCords,yCords,Dbug,btnScan)
            Touch = 0
            BUnd = false
            BRed = false
            btnUn.isEnabled = false
            btnRed.isEnabled = false
            btnScan.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
            if (Dbug) {
                Log.d("Touch cord", "Touch: $Touch")
            }
        }
    }
}