package com.example.miok_quick_response_app.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.R

// MessageContact data class
data class MessageContact(
    val name: String,
    val relationship: String,
    val phoneNumber: String

)

// MessageAdapter class
class MessageAdapter(
    private val onMessageClick: (MessageContact) -> Unit // Callback to handle message button click
) : ListAdapter<MessageContact, MessageAdapter.MessageViewHolder>(MessageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val contact = getItem(position)
        holder.nameTextView.text = contact.name
        holder.relationshipTextView.text = contact.relationship

        // Set up the click listener for the message button
        holder.messageButton.setOnClickListener {
            onMessageClick(contact) // Trigger the callback when the button is clicked
        }
    }

    override fun getItemCount(): Int = currentList.size

    // ViewHolder class to hold the references to the views in item_message.xml
    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contact_name)
        val relationshipTextView: TextView = itemView.findViewById(R.id.contact_relationship)
        val messageButton: ImageButton = itemView.findViewById(R.id.btn_message) // ImageButton for message
    }

    // DiffUtil.ItemCallback implementation for comparing MessageContact objects
    class MessageDiffCallback : DiffUtil.ItemCallback<MessageContact>() {
        override fun areItemsTheSame(oldItem: MessageContact, newItem: MessageContact): Boolean {
            // Check if both items are the same based on the name and relationship
            return oldItem.name == newItem.name && oldItem.relationship == newItem.relationship
        }

        override fun areContentsTheSame(oldItem: MessageContact, newItem: MessageContact): Boolean {
            // Check if the content of both items is the same
            return oldItem == newItem
        }
    }
}
