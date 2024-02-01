package com.fredrueda.arduino.connectarduino

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ControlActivity: AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var bluetoothManager: BluetoothManager

    val MAX_COMMANDS = 12 // Número máximo de comandos
    val commands = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l')
    val pinNumbers = intArrayOf(26, 25, 17, 16, 27, 14, 12, 13, 5, 23, 19, 18)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)

        sharedPreferences = getSharedPreferences("ButtonsData", MODE_PRIVATE)
        val address = sharedPreferences.getString("address", "")
        bluetoothManager = BluetoothManager(address!!)
        //bluetoothManager.connect()
        CoroutineScope(Dispatchers.Main).launch {
            val success = bluetoothManager.connect()
            if (!success) {
                // Maneja el caso en el que la conexión falla
            }
        }
        loadButtonsData()
    }

    private fun connectToDevice() {
        val connected = bluetoothManager.connect()
        if (!connected) {
            Log.i("Bluetooth", "Couldn't connect")
        }
    }


    private fun createButton(buttonName: String, varEncender: String) {
        val newButtonEncender = Button(this)
        newButtonEncender.text = buttonName
        newButtonEncender.setOnClickListener {
            bluetoothManager.sendCommand(varEncender)
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

        linearLayout.addView(horizontalLayout, layoutParams) // Agregar el LinearLayout horizontal al LinearLayout vertical
    }
    private fun loadButtonsData() {
        // Obtener la cantidad de botones generados previamente
        val buttonCount = sharedPreferences.getInt("buttonCount", 0)
        // Cargar los datos de cada botón generado
        for (i in 0 until buttonCount) {
            val buttonName = sharedPreferences.getString("buttonName$i", "")
            val constantVariableEncender = sharedPreferences.getString("constantVariableEncender$i", "")
            // Crear el botón y cargar los datos
            buttonName?.let { createButton(it,constantVariableEncender ?: "") }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothManager.disconnect()
    }

}