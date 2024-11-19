package com.example.miok_quick_response_app.data

import Contact
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.R

// Adapter for displaying a list of Contact objects in a RecyclerView with click listeners for remove, item, call, and message actions
class ContactAdapter(
    private val onRemoveClick: (Contact) -> Unit, // Modified to pass the actual contact
    private val onItemClick: (Contact) -> Unit, // Modified to pass the actual contact
    private val onCallClick: ((Contact) -> Unit)? = null,
    private val onMessageClick: ((Contact) -> Unit)? = null
) : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

    private var contacts = listOf<Contact>()
    private var language: String = "English" // Default language

    // Update language dynamically
    fun setLanguage(language: String) {
        this.language = language
        notifyDataSetChanged() // Refresh RecyclerView with new translations
    }

    fun updateContacts(newContacts: List<Contact>) {
        contacts = newContacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.nameTextView.text = contact.name
        holder.phone_numberTextView.text = contact.phone_number
        holder.relationshipTextView.text = contact.relationship.name.replace("_", " ")
        holder.statusTextView.text = if (contact.status) "Approved" else "Not Approved"

        holder.bind(contact, language)

        // Show or hide the "EMERGENCY CONTACT" label based on the contact's emergency status
        if (contact.emerg_contact) {
            holder.emergencyContactLabel.visibility = View.VISIBLE // Show label
        } else {
            holder.emergencyContactLabel.visibility = View.GONE // Hide label
        }

        holder.callButton.setOnClickListener { onCallClick?.invoke(contact) }
        holder.messageButton.setOnClickListener { onMessageClick?.invoke(contact) }


        holder.itemView.setOnClickListener {
            onItemClick(contact)
        } // Trigger navigation action

    }

    // Returns the number of items in the current list and binds contact data to UI elements with language-specific labels in the ViewHolder.
    override fun getItemCount(): Int = currentList.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameLabel: TextView = itemView.findViewById(R.id.contact_name_label)
        private val phoneLabel: TextView = itemView.findViewById(R.id.contact_phone_label)
        private val relationshipLabel: TextView = itemView.findViewById(R.id.contact_relationship_label)
        private val statusLabel: TextView = itemView.findViewById(R.id.contact_status_label)
        val nameTextView: TextView = itemView.findViewById(R.id.contact_name)
        val phone_numberTextView: TextView = itemView.findViewById(R.id.contact_phone_number)
        val relationshipTextView: TextView = itemView.findViewById(R.id.contact_relationship)
        val statusTextView: TextView = itemView.findViewById(R.id.contact_status)
        // Reference to the emergency contact label
        val emergencyContactLabel: TextView = itemView.findViewById(R.id.emergency_contact_label)

        val callButton: ImageButton = itemView.findViewById(R.id.contact_call_btn)
        val messageButton: ImageButton = itemView.findViewById(R.id.contact_message_btn)

        fun bind(contact: Contact, language: String) {
            // Update labels dynamically based on language
            if (language == "Māori") {
                nameLabel.text = itemView.context.getString(R.string.contact_name_label_mr)
                phoneLabel.text = itemView.context.getString(R.string.contact_phone_label_mr)
                relationshipLabel.text = itemView.context.getString(R.string.contact_relationship_label_mr)
                statusLabel.text = itemView.context.getString(R.string.contact_status_label_mr)
                emergencyContactLabel.text = itemView.context.getString(R.string.emergency_contact_label_mr)
            } else {
                nameLabel.text = itemView.context.getString(R.string.contact_name_label)
                phoneLabel.text = itemView.context.getString(R.string.contact_phone_label)
                relationshipLabel.text = itemView.context.getString(R.string.contact_relationship_label)
                statusLabel.text = itemView.context.getString(R.string.contact_status_label)
                emergencyContactLabel.text = itemView.context.getString(R.string.emergency_contact_label)
            }
        }
    }

    // DiffUtil callback to compare lists efficiently
    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            // Compare based on name, phone number, and relationship
            return oldItem.name == newItem.name && oldItem.phone_number == newItem.phone_number && oldItem.relationship == newItem.relationship
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem // Compare by all fields
        }
    }
}
