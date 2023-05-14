package com.example.poulpejeu.P2P

import android.app.IntentService
import android.content.*
import android.util.Log
import com.example.poulpejeu.GameHandler
import com.google.gson.Gson
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

            var messageToSend: String? = intent.extras!!.getString(EXTRAS_STRING)
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

                if(messageToSend == "scores"){
                    messageToSend = Gson().toJson(Message("scores", GameHandler.scores[0],GameHandler.scores[1],GameHandler.scores[2]))
                }

                outputStream.write(messageToSend + "\r\n")
                outputStream.flush()
                Log.d(TAG, "Client: Message sent, waiting for response")


                var response = inputStream.readLine()
                response.replace("\r\n","")
                Log.d(TAG, "Client: Response received : $response")
                messageHandler(response, outputStream)


            } catch (e: IOException) {
                Log.e(TAG, (e.message)!!)
            } finally {
                if (socket.isConnected) {
                    try {
                        socket.close()
                        Log.d(TAG, "Client : closing socket ")
                    } catch (e: IOException) {
                        // Give up
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun messageHandler(msg: String, outputStream: OutputStreamWriter){
        var inData: Message? = null

            try {
                inData = Gson().fromJson(msg, Message::class.java)
            }catch(e:Exception){
                e.printStackTrace()
            }
            if(inData != null){
                if (inData.messageType == "games") {
                    GameHandler.games[0] = inData.param1.toInt()
                    GameHandler.games[1] = inData.param2.toInt()
                    GameHandler.games[2] = inData.param3.toInt()

                    MessageDataHolder.serverResponseCallback?.onServerResponseReceived("start")
                }
                else if (inData.messageType == "winner") {
                    GameHandler.winner = inData.param1.toInt()
                    GameHandler.otherFinished = true
                    MessageDataHolder.serverResponseCallback?.onServerResponseReceived("finish")
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