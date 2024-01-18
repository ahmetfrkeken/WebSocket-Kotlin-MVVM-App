package com.ahmetfarukeken.websocketkotlinchatapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetfarukeken.websocketkotlinchatapp.databinding.ItemChatLeftBinding
import com.ahmetfarukeken.websocketkotlinchatapp.databinding.ItemChatRightBinding
import com.ahmetfarukeken.websocketkotlinchatapp.model.Message

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val VIEW_TYPE_LEFT = 0
        const val VIEW_TYPE_RIGHT = 1
    }

    private var messages: List<Message> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LEFT -> ChatLeftViewHolder(
                ItemChatLeftBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            VIEW_TYPE_RIGHT -> ChatRightViewHolder(
                ItemChatRightBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                )
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = messages[position].type

        when (type) {
            0 -> (holder as ChatLeftViewHolder).bind(messages[position])
            1 -> (holder as ChatRightViewHolder).bind(messages[position])
            else -> throw IllegalArgumentException("Invalid type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].type) {
            0 -> VIEW_TYPE_LEFT
            1 -> VIEW_TYPE_RIGHT
            else -> {
                VIEW_TYPE_LEFT
            }
        }
    }

    fun updateMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    class ChatLeftViewHolder(private val binding: ItemChatLeftBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
        }
    }

    class ChatRightViewHolder(private val binding: ItemChatRightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.message = message
        }
    }
}