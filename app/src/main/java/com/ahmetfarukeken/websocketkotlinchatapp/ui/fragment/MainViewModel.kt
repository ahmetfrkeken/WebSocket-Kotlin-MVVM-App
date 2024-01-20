package com.ahmetfarukeken.websocketkotlinchatapp.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ahmetfarukeken.websocketkotlinchatapp.model.Message
import com.ahmetfarukeken.websocketkotlinchatapp.ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor() : BaseViewModel(){
    private lateinit var webSocketClient: WebSocketClient

    private val webSocketError = MutableLiveData<Boolean>()
    private val webSocketMessage = MutableLiveData<List<Message>>()

    fun isWebSocketError(): LiveData<Boolean> = webSocketError
    fun getWebSocketMessage(): LiveData<List<Message>> = webSocketMessage

    fun initWebSocket() {
        val webSocketUrl = URI(MainFragment.WEB_SOCKET_URL)
        println("webSocketUrl: $webSocketUrl")
        try {
            createWebSocketClient(webSocketUrl)
        }catch (e: Exception){
            webSocketError.value = true
            println(e)
            e.printStackTrace()
        }

        //Eğer SSL sertifikalı bir websocket dinliyorsak
        //SSl ayarlamasını yapıyoruz.
        //val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        //webSocketClient.setSocketFactory(socketFactory)
    }

    fun createWebSocketClient(webSocketUrl: URI?) {
        webSocketClient = object : WebSocketClient(webSocketUrl) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(MainFragment.TAG, "onOpen")
            }

            override fun onMessage(message: String?) {
                webSocketError.value = false
                Log.d(MainFragment.TAG, "onMessage: $message")
                setUpMessage(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(MainFragment.TAG, "onClose")
            }

            override fun onError(ex: Exception?) {
                webSocketError.value = true
                Log.e(MainFragment.TAG, "onError: ${ex?.message}")
            }
        }
    }

    fun sendMessage(message: String?) {
        webSocketClient.send(
            message
        )
        webSocketMessage.postValue((webSocketMessage.value ?: listOf()) + Message(0, message ?: ""))
    }

    fun setUpMessage(message: String?) {
        webSocketMessage.postValue((webSocketMessage.value ?: listOf()) + Message(1, message ?: ""))
    }

    fun resumeWebSocket(){
        webSocketClient.connect()
    }

    fun pauseWebSocket(){
        webSocketClient.close()
    }
}