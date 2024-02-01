package com.fredrueda.arduino.connectarduino

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_select_device.*
import org.jetbrains.anko.toast

class SelectDeviceActivity : AppCompatActivity() {

    private var m_bluetoothAdapter: BluetoothAdapter? = null
    private lateinit var m_pairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        val EXTRA_ADDRESS: String = "Device_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_device)

        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (m_bluetoothAdapter == null) {
            toast("this device doesn't support bluetooth")
            return
        }
        if (!m_bluetoothAdapter!!.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        sharedPreferences = getSharedPreferences("ButtonsData", MODE_PRIVATE)

        select_device_refresh.setOnClickListener { pairedDeviceList() }
        pairedDeviceList()

    }

    private fun pairedDeviceList() {
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()
        val listNames: ArrayList<String> = ArrayList()

        if (!m_pairedDevices.isEmpty()) {
            for (device: BluetoothDevice in m_pairedDevices) {
                listNames.add(device.name)
                list.add(device)
                Log.i("device", "" + device.name)
            }
        } else {
            toast("no paired bluetooth devices found")
        }

        val adapter = MyAdapter(this, listNames, list)
        select_device_list.adapter = adapter
        select_device_list.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val device: BluetoothDevice = list[position]
                val address: String = device.address
                val editor = sharedPreferences.edit()
                editor.putString("address", address)
                editor.apply()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                if (m_bluetoothAdapter!!.isEnabled) {
                    toast("Bluetooth has been enabled")
                } else {
                    toast("Bluetooth has been disabled")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                toast("Bluetooth enabling has been canceled")
            }
        }
    }
}