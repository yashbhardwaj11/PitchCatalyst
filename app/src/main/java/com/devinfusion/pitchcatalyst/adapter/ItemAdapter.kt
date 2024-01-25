package com.devinfusion.pitchcatalyst.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.devinfusion.pitchcatalyst.R
import com.devinfusion.pitchcatalyst.model.Item
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ItemAdapter(private val onItemLongClickListener: ((Item) -> Unit)? = null) : Adapter<ItemAdapter.ItemViewHolder>() {
    private var mlist : List<Item> = arrayListOf()

    fun setLit(list : List<Item>){
        mlist = list
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(view : View) : ViewHolder(view){
        val title : TextView = view.findViewById(R.id.itemTitle)
        val desc : TextView = view.findViewById(R.id.itemDesc)
        val checkBox: CheckBox = view.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = mlist.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = mlist[position];
        holder.title.text = current.title
        holder.desc.text = current.desc
        holder.checkBox.isChecked = current.isSelected
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            current.isSelected = isChecked
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(current)
            return@setOnLongClickListener true
        }
    }



    fun getSelectedItems(): List<Item> {
        return mlist.filter { it.isSelected }
    }
}