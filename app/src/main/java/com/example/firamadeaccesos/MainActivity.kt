@file:Suppress("DEPRECATION")

package com.example.firamadeaccesos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.zxing.integration.android.IntentIntegrator
import com.mihir.drawingcanvas.drawingView

class MainActivity : AppCompatActivity() {

    // Mostrar los datos escaneados
    private var dbug = true
    // Botones activados
    private var bRed = true
    private var bUnd = true

    private var touch: Int = 0
    private var cnt: Int = 0


    private var contDat: Array<String> = arrayOf("", "")

    private lateinit var txtCuenta: TextView
    private lateinit var btnSend: Button
    private lateinit var btnUn: Button
    private lateinit var btnRed: Button
    private lateinit var btnCls: Button
    private lateinit var firma: drawingView

    private val qrFun = QrFun()
    private val dibFun = DibFun()
    private val jsonFun = JsonSend()

    private val xCords = mutableListOf<MutableList<Int>>()
    private val yCords = mutableListOf<MutableList<Int>>()

    private val xCordsGet = mutableListOf<Int>()
    private val yCordsGet = mutableListOf<Int>()

    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Firma
        firma = findViewById(R.id.drawing_view)
        dibFun.dibini(firma)//Inicializar los parametros del drawingView
        //Abre el scaner
        qrFun.qrIniLec(this)
        //TextViews
        txtCuenta = findViewById(R.id.txtCuenta)
        //Lector de QRs
        btnSend = findViewById(R.id.btnSend)
        btnSend.setOnClickListener {
            try {
                cnt--
                txtCuenta.text = "Firmas faltantes: $cnt"
                if (cnt <= 0) {//Reiniciar para resibir una nueva firma
                    Toast.makeText(this,"Firmas completadas", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
                if (dbug){// Se muestran las cordenadas guardadas
                    for ((index, filaX) in xCords.withIndex()) {
                        val filaY = yCords[index]
                        for (i in filaX.indices) {
                            Log.d("Cordenadas", "x: ${filaX[i]} y: ${filaY[i]}")
                        }
                    }
                }
                jsonFun.prosData(contDat,xCords,yCords,dbug,cnt)// Procesamiento de la informacion para el envio
                Toast.makeText(this,"Datos enviados ",Toast.LENGTH_SHORT).show()
            } catch (e: Exception){
                if (dbug){
                    Log.d("Error Send", "Error: ${e.message}")
                }
                Toast.makeText(this, "Error al enviar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                if (cnt > 0) {
                    dibFun.dibCls(firma,xCords,yCords,dbug,btnSend)
                    touch = 0
                    bUnd = false
                    bRed = false
                    btnUn.isEnabled = false
                    btnRed.isEnabled = false
                    btnSend.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
                    if (dbug) {
                        Log.d("Touch cord", "Touch: $touch")
                    }
                }
            }
        }
        //limpiar
        btnCls = findViewById(R.id.btnCls)
        btnCls.setOnClickListener {
            dibFun.dibCls(firma,xCords,yCords,dbug,btnSend)
            touch = 0
            bUnd = false
            bRed = false
            btnUn.isEnabled = false
            btnRed.isEnabled = false
            btnSend.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
            if (dbug) {
                Log.d("Touch cord", "Touch: $touch")

            }
        }
        //Deshacer
        btnUn = findViewById(R.id.btnUn)
        btnUn.setOnClickListener {
            if (bUnd) {
                dibFun.dibUn(firma,dbug)
                touch--
                if (touch>=0){
                    btnSend.isEnabled = false
                    btnSend.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
                }
                bUnd = false
                bRed = true
                btnUn.isEnabled = false
                btnRed.isEnabled = true
                if (dbug) {
                    Log.d("Touch cord", "Touch: $touch")

                }
            }
        }
        //Rehacer
        btnRed = findViewById(R.id.btnRed)
        btnRed.setOnClickListener {
            if (bRed) {
                dibFun.dibRed(firma,dbug)
                touch++
                bRed = false
                bUnd = true
                btnRed.isEnabled = false
                btnUn.isEnabled = true
                if (dbug) {
                    Log.d("Touch cord", "Touch: $touch")

                }
            }
        }

        firma.setOnTouchListener { view, event ->//
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("Touch cord", "Pulsacion")
                    bRed = false
                    bUnd = true
                    btnRed.isEnabled = false
                    btnUn.isEnabled = true
                    if (xCords.getOrNull(touch)?.isNotEmpty() == true) {
                        xCords.removeAt(touch)
                        yCords.removeAt(touch)
                        if (dbug) {
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
                        if (dbug) {
                            Log.d("Touch cord", "Coordenadas: X: $x, Y: $y")
                        }
                        btnCls.isEnabled = true
                        btnUn.isEnabled = true
                        btnRed.isEnabled = true
                        btnSend.isEnabled = true
                        btnSend.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFA5A0A0.toInt())
                    }
                }
                MotionEvent.ACTION_UP -> {
                    xCords.add(xCordsGet)
                    yCords.add(yCordsGet)
                    touch++
                    if (dbug) {
                        Log.d("Touch cord", "Fin de la pulsacion siguiente posicion: $touch")
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

    @SuppressLint("SetTextI18n")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {//Resultado del scaner
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            result?.let {
                if (it.contents.isNullOrEmpty()) {
                    Toast.makeText(this, "Qr no valido", Toast.LENGTH_SHORT).show()
                    qrFun.qrIniLec(this)
                } else {
                    val usrIn = it.contents
                    Toast.makeText(this, "Espere un poco", Toast.LENGTH_SHORT).show()
                    contDat = qrFun.qrInf(usrIn, this,dbug)//Obtencion de los datos del QR
                    if (contDat[1] == "&"){
                        cnt=5
                        txtCuenta.text = "Firmas faltantes: $cnt"
                        txtCuenta.isVisible = true
                    } else{
                        cnt=1
                        txtCuenta.isVisible = false
                    }
                }
            } ?: run {
                Toast.makeText(this, "Lectura incorrecta", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            if (dbug){
                Log.d("Error ActivityResult", "Error: ${e.message}")
            }
            Toast.makeText(this, "Error al resibir el QR: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}