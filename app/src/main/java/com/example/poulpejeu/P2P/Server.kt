package com.example.poulpejeu.P2P

import android.os.AsyncTask
import android.util.Log
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

   while(true) {
   clientSocket = serverSocket.accept()
   Log.i(WifiDirectActivity.TAG, "Server: new connection")

   inputStream = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
   outputStream = OutputStreamWriter(clientSocket.getOutputStream())


    val receivedMessage = inputStream.readLine()
    println("Received string: $receivedMessage")

    outputStream.write("yo man\r\n")
    outputStream.flush()
    serverCallback?.onServerRequestReceived("request")
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