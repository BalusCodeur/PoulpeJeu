package com.example.poulpejeu.P2P

import android.os.AsyncTask
import android.util.Log
import com.example.poulpejeu.GameHandler
import com.google.gson.Gson
import java.io.*
import java.net.ServerSocket
import java.net.Socket


class Server : AsyncTask<Void?, Void?, Void?>() {
    private lateinit var serverSocket: ServerSocket
    private lateinit var clientSocket: Socket
    private lateinit var inputStream: BufferedReader
    private lateinit var outputStream: OutputStreamWriter
    private var serverCallback: ServerCallback? = null


    override fun doInBackground(vararg params: Void?): Void? {
        serverSocket = ServerSocket(8988)
        Log.i(WifiDirectActivity.TAG, "Server: Socket opened")

        while (true) {
            clientSocket = serverSocket.accept()
            Log.i(WifiDirectActivity.TAG, "Server: new connection")

            inputStream = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
            outputStream = OutputStreamWriter(clientSocket.getOutputStream())


            var receivedMessage = inputStream.readLine()

            receivedMessage.replace("\r\n", "")

            Log.i(WifiDirectActivity.TAG, "Server - received message : $receivedMessage")

            messageHandler(receivedMessage)

        }
    }

    private fun messageHandler(msg: String) {
        var inData: Message? = null
        lateinit var outData: Message

        if (msg == "start") {
            GameHandler.pickGames()
            outData = Message(
                "games",
                GameHandler.games[0].toFloat(),
                GameHandler.games[1].toFloat(),
                GameHandler.games[2].toFloat()
            )
            outputStream.write(Gson().toJson(outData) + "\r\n")
            outputStream.flush()
            serverCallback?.onServerRequestReceived("start")
        } else {
            try {
                inData = Gson().fromJson(msg, Message::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (inData != null) {
                if (inData.messageType == "scores") {
                    GameHandler.otherScore[0] = inData.param1
                    GameHandler.otherScore[1] = inData.param2
                    GameHandler.otherScore[2] = inData.param3
                    GameHandler.otherFinished = true

                    while (!GameHandler.finished) {
                        Thread.sleep(200)
                    }
                    GameHandler.calculateWinner()
                    outData =
                        Message("winner", GameHandler.winner!!.toFloat(), 0.toFloat(), 0.toFloat())
                    outputStream.write(Gson().toJson(outData) + "\r\n")
                    outputStream.flush()
                    serverCallback?.onServerRequestReceived("finish")

                }
            }
        }
    }


    interface ServerCallback {
        fun onServerRequestReceived(request: String)
    }

    fun setServerCallback(callback: ServerCallback) {
        serverCallback = callback
    }


    private fun stopServer() {
        try {
            serverSocket.close()
            Log.d(TAG, "Server stopped")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val TAG = "ServerTask"
    }
}