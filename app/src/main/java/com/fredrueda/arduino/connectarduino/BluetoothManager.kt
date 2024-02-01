package com.fredrueda.arduino.connectarduino

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.*

class BluetoothManager(private val address: String) {
    private lateinit var bluetoothSocket: BluetoothSocket
    var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    fun connect(): Boolean {
        return try {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            val device: BluetoothDevice = bluetoothAdapter.getRemoteDevice(address)
            bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
            bluetoothSocket.connect()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    fun disconnect() {
        try {
            bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun sendCommand(input: String) {
        if (::bluetoothSocket.isInitialized && bluetoothSocket.isConnected) {
            try {
                bluetoothSocket.outputStream.write(input.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}