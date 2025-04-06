package com.example.shifumi_mobile.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.shifumi_mobile.wifi.WifiManager

@Composable
fun WifiScreen(wifiManager: WifiManager) {
    ImageBackground(0.5f)
    val messages = remember { mutableStateListOf<String>() }
    var messageText by remember { mutableStateOf(TextFieldValue("")) }
    wifiManager.checkConnectionInfo()

    // Callback que será chamado ao receber uma nova mensagem
    LaunchedEffect(Unit) {
        wifiManager.onMessageReceived = { receivedMessage ->
            messages.add("Autre: $receivedMessage")
        }
    }

    // Função para enviar mensagem
    fun sendMessage() {
        val message = messageText.text
        if (/*wifiManager.outputStream != null &&*/ message.isNotBlank()) {
            wifiManager.sendData(message)
            messages.add("YOU: $message")
            messageText = TextFieldValue("")
        } else {
            Log.e("WifiScreen", "It wasn't possible to send the message: Connection not established")
            messages.add("Error: No connection established")

        }
    }

    // Layout da tela de chat
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("💬 Chat", style = MaterialTheme.typography.headlineMedium)
        if (wifiManager.socket != null) {
            Text("Connected to: ${wifiManager.socket!!.inetAddress.hostAddress}", style = MaterialTheme.typography.bodyMedium)
        } else {
            Text("No connection established", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Área de exibição das mensagens
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            messages.forEach { message ->
                Text(text = message, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        // Caixa de entrada e botão de envio
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { sendMessage() }) {
                Text("Enviar")
            }
        }
    }
}
