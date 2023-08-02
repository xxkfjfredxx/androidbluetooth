package com.fredrueda.arduino.connectarduino

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.AsyncTask
import java.util.*
import android.bluetooth.BluetoothSocket
import android.content.SharedPreferences
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_control.*
import java.io.IOException

class ControlActivity: AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    companion object {
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        m_address = intent.getStringExtra(SelectDeviceActivity.EXTRA_ADDRESS)!!
        sharedPreferences = getSharedPreferences("ButtonsData", MODE_PRIVATE)

        ConnectToDevice(this).execute()
        //control_led_disconnect.setOnClickListener { disconnect() }
        loadButtonsData()
    }

    private fun sendCommand(input: String) {
        if (m_bluetoothSocket != null) {
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            } catch(e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun disconnect() {
        if (m_bluetoothSocket != null) {
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        finish()
    }

    private class ConnectToDevice(c: Context) : AsyncTask<Void, Void, String>() {
        private var connectSuccess: Boolean = true
        private val context: Context

        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context, "Connecting...", "please wait")
        }

        override fun doInBackground(vararg p0: Void?): String? {
            try {
                if (m_bluetoothSocket == null || !m_isConnected) {
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device: BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            } catch (e: IOException) {
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!connectSuccess) {
                Log.i("data", "couldn't connect")
            } else {
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }
    private fun createButton(buttonName: String, varEncender: String, varApagar: String) {
        val newButtonEncender = Button(this)
        val newButtonApagar = Button(this)
        newButtonEncender.text =  "$buttonName Encender"
        newButtonApagar.text =  "$buttonName Apagar"
        newButtonEncender.setOnClickListener {
            sendCommand(varEncender)
        }
        newButtonApagar.setOnClickListener {
            sendCommand(varApagar)
        }
        val linearLayout = findViewById<LinearLayout>(R.id.container) // Obtener la referencia al LinearLayout desde el layout XML
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.orientation = LinearLayout.VERTICAL // Establecer la orientación vertical
        val horizontalLayout = LinearLayout(this) // Crear un LinearLayout para los botones juntos
        horizontalLayout.orientation = LinearLayout.HORIZONTAL
        horizontalLayout.addView(newButtonEncender) // Agregar el primer botón al LinearLayout horizontal
        horizontalLayout.addView(newButtonApagar) // Agregar el segundo botón al LinearLayout horizontal

        linearLayout.addView(horizontalLayout, layoutParams) // Agregar el LinearLayout horizontal al LinearLayout vertical
    }
    private fun loadButtonsData() {
        // Obtener la cantidad de botones generados previamente
        val buttonCount = sharedPreferences.getInt("buttonCount", 0)
        // Cargar los datos de cada botón generado
        for (i in 0 until buttonCount) {
            val buttonName = sharedPreferences.getString("buttonName$i", "")
            val constantVariableEncender = sharedPreferences.getString("constantVariable$i", "")
            val constantVariableApagar = sharedPreferences.getString("constantVariable$i", "")
            // Crear el botón y cargar los datos
            buttonName?.let { createButton(it,constantVariableEncender ?: "", constantVariableApagar ?: "") }
        }
    }

}