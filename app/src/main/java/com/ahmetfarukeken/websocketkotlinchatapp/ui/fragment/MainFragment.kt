package com.ahmetfarukeken.websocketkotlinchatapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmetfarukeken.websocketkotlinchatapp.R
import com.ahmetfarukeken.websocketkotlinchatapp.databinding.DialogBottomSheetBinding
import com.ahmetfarukeken.websocketkotlinchatapp.databinding.FragmentMainBinding
import com.ahmetfarukeken.websocketkotlinchatapp.ui.adapter.ChatAdapter
import com.ahmetfarukeken.websocketkotlinchatapp.utils.WebSocketConstants
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var chatAdapter: ChatAdapter? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var bottomSheetBinding: DialogBottomSheetBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initWebSocket()
        initObserve()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        initRV()
        initBottomSheetDialog(null)
        binding.ibSend.setOnClickListener {
            viewModel.sendMessage(binding.tilMessage.editText?.text.toString())
            binding.tilMessage.editText?.text?.clear()
        }
    }

    private fun initBottomSheetDialog(error: String?) {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetDialog!!.setCancelable(true)

        bottomSheetBinding = DialogBottomSheetBinding.inflate(
            LayoutInflater.from(
                requireContext()
            ), binding.bottomSheetContainer, false
        )

        bottomSheetBinding!!.tvError.text = error ?: getString(R.string.Error)
        bottomSheetBinding!!.bDone.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }

        bottomSheetDialog!!.setContentView(bottomSheetBinding!!.root)
    }

    private fun showBottomSheetDialog(error: String?){
        bottomSheetBinding?.tvError?.text = error
        bottomSheetDialog?.show()
    }

    private fun initRV() {
        chatAdapter = ChatAdapter()
        val llm = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.apply {
            rvChat.layoutManager = llm
            rvChat.adapter = chatAdapter
        }
    }

    private fun initObserve() {
        viewModel.isWebSocketError().observe(viewLifecycleOwner, Observer {
            if (it) {
                showBottomSheetDialog(getString(R.string.there_is_no_websocket_connection))
            }
        })

        viewModel.getWebSocketMessage().observe(viewLifecycleOwner) {
            chatAdapter?.updateMessages(messages = it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.resumeWebSocket()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pauseWebSocket()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        //IPv4 connect to server url
        const val WEB_SOCKET_URL =
            "ws://${WebSocketConstants.IP_ADDRESS}:${WebSocketConstants.PORT}/chat"
        const val TAG = "WebSocketTag"
    }
}