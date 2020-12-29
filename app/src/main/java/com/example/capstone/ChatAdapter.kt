package com.example.capstone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_contact.view.*
import kotlinx.android.synthetic.main.chat.view.*

class ChatAdapter(private val chats: List<String>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chat, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBind(chats[position])
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun dataBind(chat: String) {

            itemView.tvChatItem.text = chat
//            for (i in chat.messages) {
//                itemView.tvChatItem.text = i
//            }

//            itemView.contactUserName.text = contact.username
        }
    }

}