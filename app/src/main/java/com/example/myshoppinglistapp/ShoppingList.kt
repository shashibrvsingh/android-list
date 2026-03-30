package com.example.myshoppinglistapp

import android.R
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

data class  ShoppingItem(val id: Int, var name: String, var quantity: Int, var isEditing: Boolean= false )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListApp(){
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuality by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top // Changed from Center so you can see it at the top
    ) {

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,     // Background color
                contentColor = Color.White       // Text color
            )
        ) {
            Text("Add Items")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(1.dp)
        )
        {
            // Your items here
            // Your items here
            items(sItems) { item ->
                if (item.isEditing) {
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = { editedName, editedQuantity ->
                            sItems = sItems.map { it.copy(isEditing = false) }
                            val editedItem = sItems.find { it.id == item.id }
                            editedItem?.let {
                               it.name = editedName
                                it.quantity = editedQuantity
                            }
                        }
                    )
                } else{
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            sItems= sItems.map{it.copy(isEditing = it.id == item.id)}
                        }, onDeleteClick = {
                            sItems = sItems-item
                        })
                        }
                }
            }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                 Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                     horizontalArrangement = Arrangement.SpaceAround
                     ){

                     Button(onClick = {
                         if(itemName.isNotBlank()){
                             val newItem = ShoppingItem(id = sItems.size+1,
                                 name = itemName,
                                 quantity = itemQuality.toInt())
                         sItems = sItems +  newItem

                         itemName= ""
                         itemQuality= ""
                             showDialog= false
                         }
                     })

                     { Text("Add")}
                     Button(onClick = {showDialog= false}) { Text("Cancel")}
                 }
            },
            title = { Text("Add Item") },
            text = {
                 Column {
                     OutlinedTextField(value = itemName ,
                         onValueChange = {itemName= it},
                        singleLine = true,
                         modifier = Modifier.fillMaxWidth().padding(8.dp)
                     )

                     OutlinedTextField(value = itemQuality ,
                         onValueChange = {itemQuality= it},
                         singleLine = true,
                         modifier = Modifier.fillMaxWidth().padding(8.dp)
                     )
                 }
            }
        )
    }
}
@Composable
fun ShoppingItemEditor(item: ShoppingItem , onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly){

        Column() {
            BasicTextField(value = editedName,
                onValueChange = {editedName = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )

            BasicTextField(value = editedQuantity,
                onValueChange = {editedQuantity = it},
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )

        }

        Button(onClick = {
            isEditing = false
            onEditComplete(editedName, editedQuantity.toIntOrNull()?:1)
        }) {
            Text("Save")
        }
    }
    }



@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF5F5F5))
            .border(
                BorderStroke(1.dp, Color(0xFFE0E0E0)),
                RoundedCornerShape(12.dp)
            )
            .padding(12.dp), // inner padding
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 📝 Item Name
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Text(
                text = "Qty: ${item.quantity}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        // ✏️ Edit Button
        IconButton(onClick = { onEditClick() }) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color(0xFF1976D2)
            )
        }

        // 🗑 Delete Button
        IconButton(onClick = { onDeleteClick()}) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete",
                tint = Color(0xFFD32F2F)
            )
        }

    }
}





