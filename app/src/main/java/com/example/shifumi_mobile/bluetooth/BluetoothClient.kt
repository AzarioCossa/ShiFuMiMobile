package com.example.shifumi_mobile.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.UUID

class BluetoothClient(private val context: Context, private val device: BluetoothDevice) {
    private val clientUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var socket: BluetoothSocket? = null

    fun connect() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BluetoothClient", "Permissão BLUETOOTH_CONNECT não concedida")
            // Solicite a permissão aqui, se necessário
            return
        }

        try {
            socket = device.createRfcommSocketToServiceRecord(clientUUID)
            socket?.connect()
            Log.d("BluetoothClient", "Conectado ao servidor: ${device.name}")
        } catch (e: IOException) {
            Log.e("BluetoothClient", "Erro ao conectar", e)
            try {
                socket?.close()
            } catch (closeException: IOException) {
                Log.e("BluetoothClient", "Erro ao fechar o socket", closeException)
            }
        }
    }
}