package com.fredrueda.arduino.connectarduino
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.itemlayout.view.*


/**
 * Created by Fred Rueda on 22/09/2020.
 * fredjruedao@gmail.com
 */
class MyAdapter(context: Context, items: ArrayList<String>, devices: ArrayList<BluetoothDevice>) :
    BaseAdapter() {
    private val context: Context
    private val items: ArrayList<String>
    private val devices: ArrayList<BluetoothDevice>
    private val mInflator: LayoutInflater
    init {
        this.context = context
        this.items = items
        this.devices = devices
        this.mInflator = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return devices[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = View.inflate(context, R.layout.itemlayout,null)
        view.tv.text = items.get(position)
        return view
    }


}