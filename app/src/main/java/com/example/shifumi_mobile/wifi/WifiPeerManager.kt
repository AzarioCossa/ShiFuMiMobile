package com.example.shifumi_mobile.wifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.core.content.ContextCompat

class WifiPeerManager(
    private val context: Context,
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel
) {

    fun discoverPeers() {
        try {
            checkPermissions()
            manager.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d("WifiPeerManager", "Descoberta de dispositivos iniciada")
                }

                override fun onFailure(reason: Int) {
                    Log.e("WifiPeerManager", "Falha ao iniciar descoberta de dispositivos: $reason")
                }
            })
        } catch (e: SecurityException) {
            Log.e("WifiPeerManager", "Permissões não concedidas para descobrir peers.", e)
        }
    }

    fun requestPeers() {
        try {
            checkPermissions()
            manager.requestPeers(channel) { peerList ->
                if (peerList.deviceList.isNotEmpty()) {
                    val device = peerList.deviceList.first()
                    connectToDevice(device)
                } else {
                    Log.e("WifiPeerManager", "Nenhum dispositivo encontrado")
                }
            }
        } catch (e: SecurityException) {
            Log.e("WifiPeerManager", "Permissões não concedidas para solicitar peers.", e)
        }
    }

    fun connectToDevice(device: WifiP2pDevice) {
        try {
            checkPermissions()
            val config = WifiP2pConfig().apply {
                deviceAddress = device.deviceAddress
            }

            manager.connect(channel, config, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d("WifiPeerManager", "Conectado ao dispositivo ${device.deviceName}")
                }

                override fun onFailure(reason: Int) {
                    Log.e("WifiPeerManager", "Falha ao conectar ao dispositivo: $reason")
                }
            })
        } catch (e: SecurityException) {
            Log.e("WifiPeerManager", "Permissões não concedidas para conectar ao dispositivo.", e)
        }
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