package com.example.miok_quick_response_app.data


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.R

data class MessageContact(
    val name: String,
    val relationship: String
)

class MessageAdapter : ListAdapter<MessageContact, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val contact = getItem(position)
        holder.nameTextView.text = contact.name
        holder.relationshipTextView.text = contact.relationship
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contact_name)
        val relationshipTextView: TextView = itemView.findViewById(R.id.contact_relationship)
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<MessageContact>() {
        override fun areItemsTheSame(oldItem: MessageContact, newItem: MessageContact): Boolean {
            return oldItem.name == newItem.name && oldItem.relationship == newItem.relationship
        }

        override fun areContentsTheSame(oldItem: MessageContact, newItem: MessageContact): Boolean {
            return oldItem == newItem
        }
    }
}
