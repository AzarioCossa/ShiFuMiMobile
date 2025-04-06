package com.example.shifumi_mobile.wifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.p2p.*
import android.util.Log
import androidx.core.content.ContextCompat
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class WifiClient(private val context: Context, private val manager: WifiP2pManager, private val channel: WifiP2pManager.Channel) {

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        val peers = peerList.deviceList
        if (peers.isNotEmpty()) {
            val device = peers.first()
            connectToDevice(device)
        } else {
            Log.e("WifiClient", "Nenhum dispositivo encontrado")
        }
    }

    fun discoverPeers() {
        try {
            checkPermissions()
            manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d("WifiClient", "Descoberta de dispositivos iniciada")
                }

                override fun onFailure(reason: Int) {
                    Log.e("WifiClient", "Falha ao iniciar descoberta de dispositivos: $reason")
                }
            })
        } catch (e: SecurityException) {
            Log.e("WifiClient", "Permissões não concedidas para descobrir peers.", e)
        }
    }

    private fun connectToDevice(device: WifiP2pDevice) {
        try {
            checkPermissions()
            val config = WifiP2pConfig().apply {
                deviceAddress = device.deviceAddress
            }

            manager.connect(channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d("WifiClient", "Conectado ao dispositivo ${device.deviceName}")
                }

                override fun onFailure(reason: Int) {
                    Log.e("WifiClient", "Falha ao conectar ao dispositivo: $reason")
                }
            })
        } catch (e: SecurityException) {
            Log.e("WifiClient", "Permissões não concedidas para conectar ao dispositivo.", e)
        }
    }

    fun sendMessage(ip: String, port: Int, message: String) {
        Thread {
            try {
                val socket = Socket()
                socket.connect(InetSocketAddress(ip, port), 5000)

                val outputStream: OutputStream = socket.getOutputStream()
                outputStream.write(message.toByteArray())
                outputStream.flush()

                socket.close()
                Log.d("WifiClient", "Mensagem enviada: $message")
            } catch (e: Exception) {
                Log.e("WifiClient", "Erro ao enviar mensagem", e)
            }
        }.start()
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.NEARBY_WIFI_DEVICES
        )
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                throw SecurityException("Permissão $permission não concedida")
            }
        }
    }
}