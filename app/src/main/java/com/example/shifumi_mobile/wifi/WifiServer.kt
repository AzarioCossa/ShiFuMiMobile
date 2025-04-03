package com.example.shifumi_mobile.wifi

import android.util.Log
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket

class WifiServer(private val port: Int) {

    fun startServer() {
        Thread {
            try {
                val serverSocket = ServerSocket(port)
                Log.d("WifiServer", "Servidor iniciado e aguardando conexões")

                while (true) {
                    val socket: Socket = serverSocket.accept()
                    Log.d("WifiServer", "Cliente conectado: ${socket.inetAddress}")
                    handleClient(socket)
                }
            } catch (e: IOException) {
                Log.e("WifiServer", "Erro ao iniciar servidor", e)
            }
        }.start()
    }

    private fun handleClient(socket: Socket) {
        // Aqui você pode gerenciar a comunicação com o cliente.
        // Por exemplo, criar streams de leitura e escrita para troca de dados.
        try {
            val inputStream = socket.getInputStream()
            val outputStream = socket.getOutputStream()

            // Exemplo de leitura e escrita de dados
            // ... (Implementação de leitura e escrita conforme o protocolo de comunicação)

            socket.close()
        } catch (e: IOException) {
            Log.e("WifiServer", "Erro ao comunicar com o cliente", e)
        }
    }
}
