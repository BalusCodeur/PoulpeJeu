package com.example.poulpejeu.P2P

import android.app.IntentService
import android.content.*
import android.util.Log
import java.io.*
import java.net.InetSocketAddress
import java.net.Socket

/**
 * A service that processes each string transfer request i.e Intent by opening a
 * socket connection with the WiFi Direct Group Owner and writing the string
 */
class MessageTransferService : IntentService {
    constructor(name: String?) : super(name) {}
    constructor() : super("WiFiDirectStringTransferService") {}
    private var serverResponseCallback: ServerResponseCallback? = null


    override fun onHandleIntent(intent: Intent?) {
        val context: Context = applicationContext

        if ((intent!!.action == ACTION_SEND_STRING)) {

            val messageToSend: String? = intent.extras!!.getString(EXTRAS_STRING)
            val host: String? = intent.extras!!.getString(EXTRAS_GROUP_OWNER_ADDRESS)
            val port: Int = intent.extras!!.getInt(EXTRAS_GROUP_OWNER_PORT)

            val socket = Socket()

            try {
                Log.d(TAG, "Opening client socket ")
                socket.bind(null)
                socket.connect((InetSocketAddress(host, port)), SOCKET_TIMEOUT)
                val inputStream = DataInputStream(socket.getInputStream())
                val outputStream = OutputStreamWriter(socket.getOutputStream())

                Log.d(TAG, "Client socket - " + socket.isConnected)

                outputStream.write(messageToSend)
                outputStream.flush()
                Log.d(TAG, "Client: Message sent")


                val response = inputStream.readLine()
                MessageDataHolder.serverResponseCallback?.onServerResponseReceived(response)

            } catch (e: IOException) {
                Log.e(TAG, (e.message)!!)
            } finally {
                if (socket.isConnected) {
                    try {
                        socket.close()
                        Log.d(TAG, "Closing client socket ")
                    } catch (e: IOException) {
                        // Give up
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    interface ServerResponseCallback {
        fun onServerResponseReceived(response: String)
    }


    companion object {
        private const val SOCKET_TIMEOUT: Int = 5000
        const val ACTION_SEND_STRING: String = "SEND_STRING"
        const val EXTRAS_STRING: String = "string_to_send"
        const val EXTRAS_GROUP_OWNER_ADDRESS: String = "go_host"
        const val EXTRAS_GROUP_OWNER_PORT: String = "go_port"
        const val TAG = "MessageTransferService"
    }
}