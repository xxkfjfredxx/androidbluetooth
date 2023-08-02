package com.fredrueda.arduino.connectarduino

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_create_screen.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class CreateScreen : AppCompatActivity() {

    private lateinit var container: LinearLayout
    private lateinit var editTextName: EditText
    private lateinit var addButton: Button
    private lateinit var sharedPreferences: SharedPreferences
    private var pos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_screen)
        container = findViewById(R.id.container)
        editTextName = findViewById(R.id.editTextName)
        addButton = findViewById(R.id.addButton)

        sharedPreferences = getSharedPreferences("ButtonsData", MODE_PRIVATE)

        addButton.setOnClickListener {
            val buttonName = editTextName.text.toString()
            val constantVariableEncender = editTextVariableEncender.text.toString()
            val constantVariableAPagar = editTextVariableApagar.text.toString()

            createButton(buttonName)
            saveButtonsData(buttonName, constantVariableEncender, constantVariableAPagar) // Guardar los datos de los botones
        }

        deletelast.setOnClickListener{
            val editor = sharedPreferences.edit()
            // Eliminar el nombre del botón y la variable constante del índice dado
            editor.putInt("buttonCount", pos-1)//actualizo este valor con la posision menos uno
            editor.remove("buttonName$pos")
            editor.remove("constantVariableEncender$pos")
            editor.remove("constantVariableApagar$pos")
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

    private fun createButton(buttonName: String) {
        val newButtonEncender = Button(this)
        val newButtonApagar = Button(this)
        newButtonEncender.text =  "$buttonName Encender"
        newButtonApagar.text =  "$buttonName Apagar"
        newButtonEncender.setOnClickListener {
            // Lógica para el botón generado
            // Puedes acceder a `constantVariable` aquí
        }
        newButtonApagar.setOnClickListener {
            // Lógica para el botón generado
            // Puedes acceder a `constantVariable` aquí
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

    private fun saveButtonsData(nBoton: String, encender: String, apagar: String) {
        val editor = sharedPreferences.edit()
        // Guardar los datos de cada botón generado
        // Guardar los datos en SharedPreferences
        editor.putInt("buttonCount", pos)
        editor.putString("buttonName$pos", nBoton)
        editor.putString("constantVariableEncender$pos", encender)
        editor.putString("constantVariableApagar$pos", apagar)
        editor.apply()
        pos++
    }

    private fun loadButtonsData() {
        // Obtener la cantidad de botones generados previamente
        val buttonCount = sharedPreferences.getInt("buttonCount", 0)
        // Cargar los datos de cada botón generado
        for (i in 0 until buttonCount) {
            val buttonName = sharedPreferences.getString("buttonName$i", "")
            pos = i
            // Crear el botón y cargar los datos
            buttonName?.let { createButton(it) }
        }
    }
}