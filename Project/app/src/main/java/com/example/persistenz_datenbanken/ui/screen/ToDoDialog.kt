package com.example.persistenz_datenbanken.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.persistenz_datenbanken.database.dataclass.ToDoDataClass

/**
 * Composable function to display a dialog for editing or adding a to-do item.
 *
 * This dialog allows the user to modify the name, description, due date, priority, and status of a to-do item.
 * If the provided to do is not null, the dialog allows editing an existing to-do item.
 * Otherwise, it facilitates adding a new one.
 *
 * @param todo The existing to do item to edit, or null to add a new one.
 * @param onDismiss Callback function to handle when the dialog is dismissed.
 * @param onSave Callback function to handle saving the edited or new to-do item.
 * @param onDelete Callback function to handle deleting an existing to-do item.
 */
@Composable
fun EditToDoDialog(
    todo: ToDoDataClass?,
    onDismiss: () -> Unit,
    onSave: (ToDoDataClass) -> Unit,
    onDelete: (ToDoDataClass) -> Unit
) {
    // State variables for each input field
    var name by remember { mutableStateOf(todo?.name ?: "") }
    var description by remember { mutableStateOf(todo?.description ?: "") }
    var dueDate by remember { mutableStateOf(todo?.dueDate ?: "") }
    var priority by remember { mutableStateOf(todo?.priority ?: "low") }
    var status by remember { mutableStateOf(todo?.status ?: "open") }

    // State variables to control dropdown menu visibility
    var isStatusExpanded by remember { mutableStateOf(false) }
    var isPriorityExpanded by remember { mutableStateOf(false) }

    // Context for displaying Toast messages
    val context = LocalContext.current
    val statusOptions = listOf("open", "completed") // List of possible status values
    val priorityOptions = listOf("low", "medium", "high") // List of possible priority values

    // AlertDialog to display the edit or add to-do form
    AlertDialog(
        onDismissRequest = onDismiss, // Action on dialog dismiss
        title = {
            Text(text = if (todo == null) "Add New ToDo" else "Edit ToDo") // Set dialog title based on action
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // TextField for To Do name input
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("ToDo Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                // TextField for To Do description input
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                // TextField for To Do due date input
                TextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Box for status dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = { },
                        label = { Text("Status") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { isStatusExpanded = !isStatusExpanded }) {
                                Icon(
                                    if (isStatusExpanded)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    "Toggle status dropdown"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Dropdown menu for selecting status
                    DropdownMenu(
                        expanded = isStatusExpanded,
                        onDismissRequest = { isStatusExpanded = false },
                        modifier = Modifier.width(IntrinsicSize.Min)
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                    status = option
                                    isStatusExpanded = false
                                },
                                text = { Text(option) }
                            )
                        }
                    }
                }

                // Box for priority dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = priority,
                        onValueChange = { },
                        label = { Text("Priority") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { isPriorityExpanded = !isPriorityExpanded }) {
                                Icon(
                                    if (isPriorityExpanded)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.Default.KeyboardArrowDown,
                                    "Toggle priority dropdown"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    // Dropdown menu for selecting priority
                    DropdownMenu(
                        expanded = isPriorityExpanded,
                        onDismissRequest = { isPriorityExpanded = false },
                    ) {
                        priorityOptions.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                    priority = option
                                    isPriorityExpanded = false
                                },
                                text = { Text(option) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            // Save button for confirming changes or adding a new to-do item
            Button(onClick = {
                // Validate that all fields are filled
                if (name.isBlank() || description.isBlank() || dueDate.isBlank()) {
                    Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        // Create a new or updated to-do object
                        val updatedToDo = ToDoDataClass(
                            id = todo?.id ?: 0, // Use existing ID for edit or 0 for new item
                            name = name,
                            description = description,
                            dueDate = dueDate,
                            priority = priority,
                            status = status
                        )
                        // Call the save function with the updated to-do
                        onSave(updatedToDo)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to save ToDo", Toast.LENGTH_SHORT).show()
                        android.util.Log.e("EditToDoDialog", "Error saving ToDo", e)
                    }
                }
            }) {
                Text("Save") // Text for save button
            }
        },
        dismissButton = {
            // Delete button for deleting an existing to-do item
            if (todo != null) {
                Button(onClick = {
                    try {
                        // Call delete function with the current to-do item
                        onDelete(todo)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to delete ToDo", Toast.LENGTH_SHORT).show()
                        android.util.Log.e("EditToDoDialog", "Error deleting ToDo", e)
                    }
                }) {
                    Text("Delete") // Text for delete button
                }
            }
        }
    )
}
