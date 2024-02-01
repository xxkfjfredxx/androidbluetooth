package com.fredrueda.arduino.connectarduino

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_create_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateScreen : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var editTextName: EditText
    private lateinit var addButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private var pos = 0
    private lateinit var bluetoothManager: BluetoothManager

    val MAX_COMMANDS = 12 // Número máximo de comandos
    val commands = charArrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l')
    val pinNumbers = intArrayOf(26, 25, 17, 16, 27, 14, 12, 13, 5, 23, 19, 18)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_screen)
        container = findViewById(R.id.container)
        editTextName = findViewById(R.id.editTextName)
        addButton = findViewById(R.id.addButton)

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

        addButton.setOnClickListener {
            val buttonName = editTextName.text.toString()
            val constantVariableEncender = editTextVariableEncender.text.toString()

            createButton(buttonName, constantVariableEncender)
            saveButtonsData(buttonName, constantVariableEncender) // Guardar los datos de los botones
        }

        deletelast.setOnClickListener{
            val editor = sharedPreferences.edit()
            // Eliminar el nombre del botón y la variable constante del índice dado
            editor.putInt("buttonCount", pos-1)//actualizo este valor con la posision menos uno
            editor.remove("buttonName$pos")
            editor.remove("constantVariableEncender$pos")
            // Aplicar los cambios
            editor.apply()
        }

        clearall.setOnClickListener{
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            container.removeAllViews()
        }

        loadButtonsData() // Cargar los datos de los botones al abrir la app
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

    private fun saveButtonsData(nBoton: String, encender: String) {
        val editor = sharedPreferences.edit()
        // Guardar los datos de cada botón generado
        // Guardar los datos en SharedPreferences
        editor.putInt("buttonCount", pos+1)
        editor.putString("buttonName$pos", nBoton)
        editor.putString("constantVariableEncender$pos", encender)
        editor.apply()
        pos++
    }

    private fun loadButtonsData() {
        // Obtener la cantidad de botones generados previamente
        val buttonCount = sharedPreferences.getInt("buttonCount", 0)
        // Cargar los datos de cada botón generado
        for (i in 0 until buttonCount) {
            val buttonName = sharedPreferences.getString("buttonName$i", "") ?: ""
            val constantVariableEncender = sharedPreferences.getString("constantVariableEncender$i", "") ?: ""
            pos = i
            // Crear el botón y cargar los datos
            createButton(buttonName,constantVariableEncender)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothManager.disconnect()
    }

}