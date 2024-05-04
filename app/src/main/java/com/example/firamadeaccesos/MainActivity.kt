@file:Suppress("DEPRECATION")

package com.example.firamadeaccesos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
    /*Configuraciones*/
    private val config = Config()
    private val logDbug = LogDbug()

    private val xCords = mutableListOf<MutableList<Int>>()
    private val yCords = mutableListOf<MutableList<Int>>()

    private val xCordsGet = mutableListOf<Int>()
    private val yCordsGet = mutableListOf<Int>()

    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Lista
        xCords.add(mutableListOf())
        yCords.add(mutableListOf())
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
                if (cnt <= 0) {//Reiniciar para resibir una nueva firma
                    config.reset(this)
                }
                txtCuenta.text = "Firmas faltantes: $cnt"
                jsonFun.prosData(contDat,xCords,yCords,cnt)// Procesamiento de la informacion para el envio
                xCords.clear()
                yCords.clear()
            } catch (e: Exception){
                logDbug.error("btnSend",e.message)
                Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
            } finally {
                if (cnt > 0) {
                    dibFun.dibCls(firma,xCords,yCords,btnSend)
                    touch = 0
                    bUnd = false
                    bRed = false
                    btnUn.isEnabled = false
                    btnRed.isEnabled = false
                    btnSend.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
                    logDbug.touchMSG(touch,"")
                }
            }
        }
        //limpiar
        btnCls = findViewById(R.id.btnCls)
        btnCls.setOnClickListener {
            dibFun.dibCls(firma,xCords,yCords,btnSend)
            touch = 0
            bUnd = false
            bRed = false
            btnUn.isEnabled = false
            btnRed.isEnabled = false
            btnSend.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
            logDbug.touchMSG(touch,"")
        }
        //Deshacer
        btnUn = findViewById(R.id.btnUn)
        btnUn.setOnClickListener {
            if (bUnd) {
                dibFun.dibUn(firma)
                touch--
                if (touch>=0){
                    btnSend.isEnabled = false
                    btnSend.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFF736E6E.toInt())
                }
                bUnd = false
                bRed = true
                btnUn.isEnabled = false
                btnRed.isEnabled = true
                logDbug.touchMSG(touch,"")
            }
        }
        //Rehacer
        btnRed = findViewById(R.id.btnRed)
        btnRed.setOnClickListener {
            if (bRed) {
                dibFun.dibRed(firma)
                touch++
                bRed = false
                bUnd = true
                btnRed.isEnabled = false
                btnUn.isEnabled = true
                logDbug.touchMSG(touch,"")
            }
        }

        firma.setOnTouchListener { view, event ->//
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    bRed = false
                    bUnd = true
                    btnRed.isEnabled = false
                    btnUn.isEnabled = true
                    if (xCords[touch].isNotEmpty() && xCords[touch].isNotEmpty()) {
                        xCords[touch].clear()
                        yCords[touch].clear()
                        logDbug.touchMSG(touch,", Posición sobreescrita")
                    } else {
                        logDbug.touchMSG(touch,", nueva lista")
                        xCords.add(mutableListOf())
                        yCords.add(mutableListOf())
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    if (x >= 0 && x <= firma.width && y >= 0 && y <= firma.height) {
                        xCordsGet.add(x)
                        yCordsGet.add(y)
                        logDbug.cordsAsGet(x,y)
                        btnCls.isEnabled = true
                        btnUn.isEnabled = true
                        btnRed.isEnabled = false
                        btnSend.isEnabled = true
                        btnSend.backgroundTintList = android.content.res.ColorStateList.valueOf(0xFFA5A0A0.toInt())
                    }
                }
                MotionEvent.ACTION_UP -> {
                    xCords[touch].addAll(xCordsGet)
                    yCords[touch].addAll(yCordsGet)
                    xCordsGet.clear()
                    yCordsGet.clear()
                    touch++
                    logDbug.touchEnd(touch)
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
                    contDat = qrFun.qrInf(usrIn, this)//Obtencion de los datos del QR
                    if (contDat[1] == "&"){
                        cnt=5
                        txtCuenta.text = "Firmas faltantes: $cnt"
                        txtCuenta.isVisible = true
                    } else{
                        cnt=1
                        txtCuenta.text = ""
                        txtCuenta.isVisible = false
                    }
                }
            } ?: run {
                Toast.makeText(this, "Lectura incorrecta", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            logDbug.error("ActivityResult",e.message)
            Toast.makeText(this, "Error al resibir el QR", Toast.LENGTH_SHORT).show()
        }
    }
}