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

class ContactAdapter(
    private val onRemoveClick: (Int) -> Unit
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
/*
        holder.removeButton.setOnClickListener {
            onRemoveClick(position)
        }*/
    }

    override fun getItemCount(): Int = currentList.size

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.contact_name)
        val phone_numberTextView: TextView = itemView.findViewById(R.id.contact_phone_number)
        val relationshipTextView: TextView = itemView.findViewById(R.id.contact_relationship)
        //val removeButton: ImageButton = itemView.findViewById(R.id.remove_button)
    }

    // DiffUtil callback to compare lists efficiently
    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            // Compare based on name, dob, and relationship instead of id
            return oldItem.name == newItem.name && oldItem.phone_number == newItem.phone_number && oldItem.relationship == newItem.relationship
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem // Compare by all fields
        }
    }

}
