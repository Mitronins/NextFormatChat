package com.addd.nextformatchat

/**
 * Created by addd on 16.02.2018.
 */

import com.addd.nextformatchat.model.EventAuth
import com.google.gson.reflect.TypeToken
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.io.IOException

class MyWebSocket(id: Int) : WebSocketListener() {
    private val id: Int = id
    private lateinit var socket: WebSocket
    private var isWrong = false
    private var socketCallback: SocketCallback? = null
    override fun onOpen(myWebSocket: WebSocket, response: Response) {
        socket = myWebSocket
    }

    fun run() {
        val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build()

        val request = Request.Builder()
                .url("http://176.57.215.171/ws/connect")
                .build()
        val websocket = client.newWebSocket(request, this)
        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown()
        print("asdasdasd")
    }

    override fun onMessage(myWebSocket: WebSocket, text: String) {
        val type = object : TypeToken<EventAuth>() {
        }.type
        val event = gson.fromJson<EventAuth>(text, type)
        if (event.event == "success_auth") {
            myWebSocket.send("{\"event\":\"auth\", \"data\" : { \"id\" : $id}}")
        }
        socketCallback?.message(text)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("MESSAGE: " + bytes.hex())
    }

    override fun onClosing(myWebSocket: WebSocket, code: Int, reason: String) {
        myWebSocket.close(1000, null)
        println("CLOSE: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        t?.printStackTrace()
        try {
            Thread.sleep(1000)
        } catch (e: IOException) {
            run()
        }
        run()
    }

    interface SocketCallback {
        fun message(text: String)
    }

    fun registerSocketCallback(callback: SocketCallback?) {
        this.socketCallback = callback
    }

    fun close() {
        socket.close(1000, "Goodbye, World!")
    }
}
