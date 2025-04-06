package com.example.shifumi_mobile.wifi

import android.content.Context
import android.net.wifi.p2p.*
import android.util.Log
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class WifiManager(private val context: Context) {
    private var socket: Socket? = null
    private var serverSocket: ServerSocket? = null
    public var outputStream: OutputStream? = null
    private var inputStream: BufferedReader? = null
    private var isServerRunning = false
    // Callback para tratar mensagens recebidas
    var onMessageReceived: ((String) -> Unit)? = null

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    // Wi-Fi Direct Manager
    private val manager: WifiP2pManager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    private val channel: WifiP2pManager.Channel = manager.initialize(context, context.mainLooper, null)

    fun checkConnectionInfo() {
        manager.requestConnectionInfo(channel) { info ->
            if (info.groupFormed) {
                if (info.isGroupOwner) {
                    // O dispositivo é o servidor (Group Owner)
                    Log.d("WifiManager", "Sou o servidor! Iniciando o servidor.")
                    startServer()  // Inicia o servidor
                } else {
                    // O dispositivo é o cliente
                    Log.d("WifiManager", "Sou o cliente! Conectando ao servidor.")
                    connectToServer(info.groupOwnerAddress.hostAddress)  // Conecta ao servidor
                }
            } else {
                Log.e("WifiManager", "Não há grupo formado.")
            }
        }
    }

    fun startServer(port: Int = 8888) {
        if (!isServerRunning) {
            isServerRunning = true
            executor.execute {
                try {
                    serverSocket = ServerSocket(port)
                    Log.d("WifiManager", "Aguardando conexão...")
                    socket = serverSocket!!.accept()
                    Log.d("WifiManager", "Cliente conectado: ${socket!!.inetAddress.hostAddress}")
                    setupStreams()
                    listenForMessages()
                } catch (e: IOException) {
                    Log.e("WifiManager", "Erro ao iniciar servidor: ${e.message}")
                    isServerRunning = false
                }
            }
        } else {
            Log.d("WifiManager", "Servidor já está em execução.")
        }
    }

    fun connectToServer(ip: String, port: Int = 8888) {
        // Verifica se o socket já está conectado
        if (socket?.isConnected == true) {
            Log.d("WifiManager", "Já está conectado ao servidor: ${socket?.inetAddress?.hostAddress}")
            return  // Não faz nada se já estiver conectado
        }

        // Caso o socket não esteja conectado, tenta conectar
        executor.execute {
            try {
                // Cria o socket e tenta conectar
                socket = Socket(ip, port)
                Log.d("WifiManager", "Conectado ao servidor: $ip")

                // Configura os fluxos de entrada e saída
                setupStreams()

                // Inicia a escuta de mensagens
                //listenForMessages()

            } catch (e: IOException) {
                Log.e("WifiManager", "Erro ao conectar: ${e.message}")
            }
        }
    }


    private fun setupStreams() {
        try {
            outputStream = socket?.getOutputStream()
            inputStream = BufferedReader(InputStreamReader(socket?.getInputStream()))
        } catch (e: IOException) {
            Log.e("WifiManager", "Erro ao configurar streams: ${e.message}")
        }
    }

    fun sendData(message: String) {
        Log.d("WifiManager", "Mensagem a enviar: $message")
        // Verifica se a conexão está ativa
        listenForMessages()
        if (socket != null && socket!!.isConnected && outputStream != null) {
            executor.execute {
                try {
                    Log.d("WifiManager", "Tentando enviar mensagem: $message")
                    outputStream?.write((message + "\n").toByteArray())
                    Log.d("WifiManager", "Mensagem enviada: $message")
                    outputStream?.flush()
                    Log.d("WifiManager", "Mensagem enviada com sucesso.")
                } catch (e: IOException) {
                    Log.e("WifiManager", "Erro ao enviar mensagem: ${e.message}")
                    // Tente reconectar ou tratar o erro aqui
                    handleBrokenPipe()
                }
            }
        } else {
            Log.e("WifiManager", "Erro: socket ou outputStream não está disponível.")
        }
    }

    fun handleBrokenPipe() {
        // Aqui, você pode tentar reconectar ou fechar o socket corretamente
        Log.e("WifiManager", "Tentando reconectar...")
        reconnect()
    }

    fun reconnect() {
        // Se necessário, reconecte o socket e reestabeleça os fluxos
        if (socket != null && !socket!!.isConnected) {
            connectToServer("endereco_ip", 8888)
        }
    }



    private fun listenForMessages() {
        executor.execute {
            try {
                if (!socket?.isClosed!!) { // Verifica se o socket não foi fechado
                    val message = inputStream?.readLine()// Sai se não houver mais dados
                    //messages.add("Other: ", message)
                    Log.d("WifiManager", "Mensagem recebida: $message")
                    if (message != null) {
                        onMessageReceived?.invoke(message)
                    }
                }
            } catch (e: IOException) {
                Log.e("WifiManager", "Erro ao receber mensagem: ${e.message}")
            }
        }
    }


    fun closeConnection() {
        try {
            inputStream?.close()
            outputStream?.close()
            socket?.close()
            serverSocket?.close()
        } catch (e: IOException) {
            Log.e("WifiManager", "Erro ao fechar conexão: ${e.message}")
        }
    }
}
