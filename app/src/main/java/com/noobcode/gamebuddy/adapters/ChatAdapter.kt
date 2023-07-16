package com.noobcode.gamebuddy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.R
import com.noobcode.gamebuddy.databinding.ItemChatSelfBinding
import com.noobcode.gamebuddy.databinding.ItemChatThemBinding

class ChatAdapter(var messages: MutableList<ObjectModels.ChatData>): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    val MSG_TYPE_LEFT = 0
    val MSG_TYPE_RIGHT = 1

    fun updateList(tempMessages: MutableList<ObjectModels.ChatData>){
        messages.clear()
        messages = tempMessages
        notifyDataSetChanged()
    }

    fun addMessage(tempMessage: ObjectModels.ChatData){
        messages.add(tempMessage)
        notifyItemInserted(messages.size-1)
    }

    fun deleteMessage(tempMessage: ObjectModels.ChatData){
        val itemIdx = messages.indexOf(tempMessage)
        messages.remove(tempMessage)
        notifyItemRemoved(itemIdx)
    }

    inner class ChatViewHolder(var itemView: View) : ViewHolder(itemView){
        var messageText = itemView.findViewById<TextView>(R.id.text_message_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view: View

        if (viewType == MSG_TYPE_RIGHT){
            view = ItemChatSelfBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        }else{
            view = ItemChatThemBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
        }
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.messageText.text = messages[position].message
    }

    override fun getItemViewType(position: Int): Int {
        if(messages[position].sender == FirebaseObject.currentUser.uid)
            return MSG_TYPE_RIGHT
        else
            return MSG_TYPE_LEFT
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}