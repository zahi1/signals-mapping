package com.example.project_mis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_mis.R

class MacAddressAdapter(
    private val onDelete: (String) -> Unit,
    private val onEdit: (Int, String) -> Unit
) : RecyclerView.Adapter<MacAddressAdapter.MacAddressViewHolder>() {

    private val macList = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MacAddressViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.mac_address_item, parent, false)
        return MacAddressViewHolder(view)
    }

    override fun onBindViewHolder(holder: MacAddressViewHolder, position: Int) {
        holder.bind(macList[position], position, onDelete, onEdit)
    }

    override fun getItemCount(): Int = macList.size

    fun addMacAddress(macAddress: String) {
        macList.add(macAddress)
        notifyItemInserted(macList.size - 1)
    }

    fun updateMacAddress(index: Int, newMac: String) {
        macList[index] = newMac
        notifyItemChanged(index)
    }

    fun deleteMacAddress(macAddress: String) {
        val index = macList.indexOf(macAddress)
        if (index != -1) {
            macList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class MacAddressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            macAddress: String,
            position: Int,
            onDelete: (String) -> Unit,
            onEdit: (Int, String) -> Unit
        ) {
            val macAddressText = itemView.findViewById<TextView>(R.id.mac_address_text)
            macAddressText.text = macAddress

            itemView.findViewById<Button>(R.id.delete_button).setOnClickListener {
                onDelete(macAddress)
            }

            itemView.findViewById<Button>(R.id.edit_button).setOnClickListener {
                onEdit(position, macAddress)
            }
        }
    }
}
