package com.example.miok_quick_response_app.data

import Contact
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miok_quick_response_app.ContactFragmentDirections

import com.example.miok_quick_response_app.R

class ContactAdapter(
    private val onRemoveClick: (Contact) -> Unit, // Modified to pass the actual contact
    private val onItemClick: (Contact) -> Unit // Modified to pass the actual contact
) : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(ContactDiffCallback()) {

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
        //holder.emergencyContactLabel.text = if (contact.emerg_contact) "EMERGENCY CONTACT" else ""

        // Show or hide the "EMERGENCY CONTACT" label based on the contact's emergency status
        if (contact.emerg_contact) {
            holder.emergencyContactLabel.visibility = View.VISIBLE // Show label
        } else {
            holder.emergencyContactLabel.visibility = View.GONE // Hide label
        }


        holder.itemView.setOnClickListener {
            onItemClick(contact)
        } // Trigger navigation action

        // Handling the swipe to delete or remove contact
        //holder.itemView.setOnClickListener {
            // Trigger removal of the contact when item is clicked (could be swipe if needed)
          //  onRemoveClick(contact)
        //}
    }

    override fun getItemCount(): Int = currentList.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contact_name)
        val phone_numberTextView: TextView = itemView.findViewById(R.id.contact_phone_number)
        val relationshipTextView: TextView = itemView.findViewById(R.id.contact_relationship)
        val statusTextView: TextView = itemView.findViewById(R.id.contact_status)
        // Reference to the emergency contact label
        val emergencyContactLabel: TextView = itemView.findViewById(R.id.emergency_contact_label)
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
