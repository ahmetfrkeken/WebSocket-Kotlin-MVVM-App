package com.ahmetfarukeken.websocketkotlinchatapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetfarukeken.websocketkotlinchatapp.databinding.FragmentMainBinding
import com.ahmetfarukeken.websocketkotlinchatapp.model.Message
import com.ahmetfarukeken.websocketkotlinchatapp.ui.adapter.ChatAdapter
import com.ahmetfarukeken.websocketkotlinchatapp.utils.WebSocketConstants
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var webSocketClient: WebSocketClient
    private var messages = ArrayList<Message>()
    private var chatAdapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWebSocket()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        chatAdapter = ChatAdapter()
        val llm = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.apply {
            rvChat.layoutManager = llm
            rvChat.adapter = chatAdapter
        }
        binding.ibSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun initWebSocket() {
        val webSocketUrl: URI? = URI(WEB_SOCKET_URL)
        println("webSocketUrl: $webSocketUrl")
        createWebSocketClient(webSocketUrl)

        //Eğer SSL sertifikalı bir websocket dinliyorsak
        //SSl ayarlamasını yapıyoruz.
        //val socketFactory: SSLSocketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory
        //webSocketClient.setSocketFactory(socketFactory)
    }

    private fun createWebSocketClient(webSocketUrl: URI?) {
        webSocketClient = object : WebSocketClient(webSocketUrl) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage: $message")
                setUpMessage(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose")
            }

            override fun onError(ex: Exception?) {
                Log.e(TAG, "onError: ${ex?.message}")
            }
        }
    }

    private fun sendMessage() {
        webSocketClient.send(
            binding.tilMessage.editText?.text.toString()
        )
        messages.add(Message(0, binding.tilMessage.editText?.text.toString()))
        chatAdapter?.updateMessages(messages = messages)
        binding.tilMessage.editText?.text?.clear()
    }

    private fun setUpMessage(message: String?) {
        requireActivity().runOnUiThread {
            messages.add(Message(1, message ?: ""))
            chatAdapter?.updateMessages(messages = messages)
        }
    }

    override fun onResume() {
        super.onResume()
        webSocketClient.connect()
    }

    override fun onPause() {
        super.onPause()
        webSocketClient.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        //IPv4 connect to server url
        const val WEB_SOCKET_URL = "ws://${WebSocketConstants.IP_ADDRESS}:${WebSocketConstants.PORT}/chat"
        const val TAG = "WebSocketTag"
    }
}