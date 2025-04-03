package com.example.shifumi_mobile.ui

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shifumi_mobile.wifi.WifiManager

@Composable
fun WifiScreen(wifiManager: WifiManager) {
    val devices = remember { mutableStateListOf<WifiP2pDevice>() }
    val isLoading = remember { mutableStateOf(true) }

    // Criar o listener para receber os dispositivos encontrados
    val peerListListener = remember {
        WifiP2pManager.PeerListListener { peerList ->
            devices.clear()
            devices.addAll(peerList.deviceList)
            isLoading.value = false // Stop loading after fetching peers
        }
    }

    // Descobrir dispositivos ao abrir a tela
    LaunchedEffect(Unit) {
        wifiManager.startPeerDiscovery()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("📡 Dispositivos Encontrados:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        // Show loading spinner while discovering peers
        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Show devices found
            if (devices.isEmpty()) {
                Text("Nenhum dispositivo encontrado", style = MaterialTheme.typography.bodyLarge)
            } else {
                devices.forEach { device ->
                    Text(
                        text = "📶 ${device.deviceName}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { wifiManager.connectToDevice(device) }
                    )
                }
            }
        }
    }
}
