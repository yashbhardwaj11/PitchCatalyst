package com.devinfusion.pitchcatalyst

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.devinfusion.pitchcatalyst.adapter.ItemAdapter
import com.devinfusion.pitchcatalyst.databinding.ActivityMainBinding
import com.devinfusion.pitchcatalyst.model.Item
import com.devinfusion.pitchcatalyst.viewmodel.ItemViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var dbRef : CollectionReference
    private lateinit var adapter : ItemAdapter
    private lateinit var itemViewModel: ItemViewModel
    private lateinit var selectedItems : List<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()
        dbRef = db.collection("item")
        selectedItems = listOf()

        itemViewModel = ViewModelProvider(this).get(ItemViewModel::class.java)

        adapter = ItemAdapter { selectedItems ->
            showMaterialInputDialogUser(selectedItems)
        }
        binding.mainRV.layoutManager = LinearLayoutManager(this)
        binding.mainRV.adapter = adapter


        itemViewModel.items.observe(this) { items ->
            Log.d("MainActivity", "Items received: $items")
            adapter.setLit(items)
        }


        binding.addButton.setOnClickListener {
            showMaterialInputDialog()
        }

        binding.deleteButton.setOnClickListener{
            selectedItems = adapter.getSelectedItems()
            if(selectedItems.isNotEmpty()){
                itemViewModel.deleteSelectedItems(selectedItems)
            }else{
                Toast.makeText(this@MainActivity, "Nothing to delete", Toast.LENGTH_SHORT).show()
            }
        }


    }
    private fun showMaterialInputDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Enter Details")
        val customLayout = layoutInflater.inflate(R.layout.custom_dialog, null)
        val titleEditText = customLayout.findViewById<TextInputEditText>(R.id.titleEditText)
        val descEditText = customLayout.findViewById<TextInputEditText>(R.id.descEditText)

        builder.setView(customLayout)

        builder.setPositiveButton("Add") { _, _ ->
            val titleText = titleEditText.text.toString()
            val descText = descEditText.text.toString()

            if (titleText.isNotEmpty() && descText.isNotEmpty()) {
                val _id = dbRef.document().id
                val item = Item(titleText,descText,_id,false,System.currentTimeMillis())
                dbRef.document(_id).set(item).addOnCompleteListener {
                    Toast.makeText(this@MainActivity, "Item Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Empty text not allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

    private fun showMaterialInputDialogUser(selectedItems: Item) {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Enter Details")
        val customLayout = layoutInflater.inflate(R.layout.custom_dialog, null)
        val titleEditText = customLayout.findViewById<TextInputEditText>(R.id.titleEditText)
        val descEditText = customLayout.findViewById<TextInputEditText>(R.id.descEditText)

        titleEditText.setText( selectedItems.title)
        descEditText.setText( selectedItems.desc)

        builder.setView(customLayout)

        builder.setPositiveButton("Add") { _, _ ->
            val titleText = titleEditText.text.toString()
            val descText = descEditText.text.toString()

            if (titleText.isNotEmpty() && descText.isNotEmpty()) {
                val _id = selectedItems._id
                val item = Item(titleText,descText,_id,false,System.currentTimeMillis())
                dbRef.document(_id!!).set(item).addOnCompleteListener {
                    Toast.makeText(this@MainActivity, "Item Added", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@MainActivity, "Empty text not allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }

}