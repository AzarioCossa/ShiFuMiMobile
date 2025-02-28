package com.example.shifumi_mobile.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.UUID

class BluetoothServer(private val context: Context) {
    private val serverUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var serverSocket: BluetoothServerSocket? = null

    fun startServer(bluetoothAdapter: BluetoothAdapter) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Log.e("BluetoothServer", "Permissão BLUETOOTH_CONNECT não concedida")
            return
        }

        try {
            serverSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord("ShifumiGame", serverUUID)
            Log.d("BluetoothServer", "Servidor aguardando conexões...")

            val socket: BluetoothSocket? = serverSocket?.accept()
            if (socket != null) {
                Log.d("BluetoothServer", "Conexão aceita de: ${socket.remoteDevice.name}")
                serverSocket?.close() // Fecha o socket do servidor
            }
        } catch (e: IOException) {
            Log.e("BluetoothServer", "Erro ao iniciar o servidor", e)
        }
    }
}