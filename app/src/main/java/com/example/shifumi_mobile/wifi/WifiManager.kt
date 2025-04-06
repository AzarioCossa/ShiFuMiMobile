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
    public var socket: Socket? = null
    private var serverSocket: ServerSocket? = null
    public var outputStream: OutputStream? = null
    private var inputStream: BufferedReader? = null
    private var isServerRunning = false
    var onMessageReceived: ((String) -> Unit)? = null

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()

    private val manager: WifiP2pManager = context.getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
    private val channel: WifiP2pManager.Channel = manager.initialize(context, context.mainLooper, null)

    fun checkConnectionInfo() {
        manager.requestConnectionInfo(channel) { info ->
            if (info.groupFormed) {
                if (info.isGroupOwner) {
                    Log.d("WifiManager", "I am the server! Starting server.")
                    startServer()
                } else {
                    // O dispositivo é o cliente
                    Log.d("WifiManager", "I am a client! Connecting to the server.")
                    connectToServer(info.groupOwnerAddress.hostAddress)  // Conecta ao servidor
                }
            } else {
                Log.e("WifiManager", "No existing group.")
            }
        }
    }

    fun startServer(port: Int = 8888) {
        if (!isServerRunning) {
            isServerRunning = true
            executor.execute {
                try {
                    serverSocket = ServerSocket(port)
                    Log.d("WifiManager", "Awaiting connection...")
                    socket = serverSocket!!.accept()
                    Log.d("WifiManager", "Client connected: ${socket!!.inetAddress.hostAddress}")
                    setupStreams()
                    listenForMessages()
                } catch (e: IOException) {
                    Log.e("WifiManager", "Error when starting server: ${e.message}")
                    isServerRunning = false
                }
            }
        } else {
            Log.d("WifiManager", "Server already in execution.")
        }
    }

    fun connectToServer(ip: String, port: Int = 8888) {
        if (socket?.isConnected == true) {
            Log.d("WifiManager", "Already connected with server: ${socket?.inetAddress?.hostAddress}")
            return
        }

        executor.execute {
            try {

                socket = Socket(ip, port)
                Log.d("WifiManager", "Connected to server: $ip")

                setupStreams()

                // Inicia a escuta de mensagens
                //listenForMessages()

            } catch (e: IOException) {
                Log.e("WifiManager", "Error while connecting: ${e.message}")
            }
        }
    }


    private fun setupStreams() {
        try {
            outputStream = socket?.getOutputStream()
            inputStream = BufferedReader(InputStreamReader(socket?.getInputStream()))
        } catch (e: IOException) {
            Log.e("WifiManager", "Error setting up streams: ${e.message}")
        }
    }

    fun sendData(message: String) {
        Log.d("WifiManager", "Message to send: $message")
        listenForMessages()
        if (socket != null && socket!!.isConnected && outputStream != null) {
            executor.execute {
                try {
                    Log.d("WifiManager", "Trying to send message : $message")
                    outputStream?.write((message + "\n").toByteArray())
                    Log.d("WifiManager", "Message sent: $message")
                    outputStream?.flush()
                    Log.d("WifiManager", "Message succesfuly sent.")
                } catch (e: IOException) {
                    Log.e("WifiManager", "Error while trying to send message: ${e.message}")
                    handleBrokenPipe()
                }
            }
        } else {
            Log.e("WifiManager", "Error: socket or outputStream not available.")
        }
    }

    fun handleBrokenPipe() {
        // Aqui, você pode tentar reconectar ou fechar o socket corretamente
        Log.e("WifiManager", "Trying to reconnect...")
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
