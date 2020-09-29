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
import android.util.Log
import kotlinx.android.synthetic.main.activity_control.*
import java.io.IOException



class ControlActivity: AppCompatActivity() {

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

        ConnectToDevice(this).execute()

        seg_one.setOnClickListener { sendCommand("o") }
        seg_two.setOnClickListener { sendCommand("p") }
        seg_three.setOnClickListener { sendCommand("q") }
        control_led_on.setOnClickListener { sendCommand("a") }
        control_led_off.setOnClickListener { sendCommand("b") }
        led_four_on.setOnClickListener { sendCommand("c") }
        led_four_off.setOnClickListener { sendCommand("d") }
        led_five_on.setOnClickListener { sendCommand("e") }
        led_five_off.setOnClickListener { sendCommand("f") }
        led_eigth_on.setOnClickListener { sendCommand("g") }
        led_eigth_off.setOnClickListener { sendCommand("h") }
        led_ten_on.setOnClickListener { sendCommand("i") }
        led_ten_off.setOnClickListener { sendCommand("j") }
        all_off.setOnClickListener { sendCommand("k") }
        all_on.setOnClickListener { sendCommand("l") }
        reset.setOnClickListener { sendCommand("m") }
        control_led_disconnect.setOnClickListener { disconnect() }
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
}