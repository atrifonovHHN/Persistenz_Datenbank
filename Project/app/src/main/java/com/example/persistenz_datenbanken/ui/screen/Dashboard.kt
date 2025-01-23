package com.example.persistenz_datenbanken.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.persistenz_datenbanken.database.dataclass.ToDoDataClass
import com.example.persistenz_datenbanken.viewmodel.ToDoViewModel

/**
 * The main dashboard screen that displays the list of ToDos
 *
 * It provides the user with options to view and manage open and completed tasks
 * The tasks are organized in tabs for open and completed tasks
 *
 * @param viewModel the [ToDoViewModel] to manage data operations for ToDos
 */
@Composable
fun DashboardScreen(
    viewModel: ToDoViewModel = viewModel()
) {
    LocalContext.current
    val todos by viewModel.todos.observeAsState(emptyList())
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedToDo by remember { mutableStateOf<ToDoDataClass?>(null) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val openTodos = todos.filter { it.status == "open" }
    val completedTodos = todos.filter { it.status == "completed" }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Dashboard",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                selectedToDo = null
                showEditDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add ToDo")
            }
        }

        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(selected = selectedTabIndex == 0, onClick = { selectedTabIndex = 0 }) {
                Text("Open Tasks")
            }
            Tab(selected = selectedTabIndex == 1, onClick = { selectedTabIndex = 1 }) {
                Text("Completed Tasks")
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val currentTodos = if (selectedTabIndex == 0) openTodos else completedTodos
            items(currentTodos) { todo ->
                TodoCard(
                    todo = todo,
                    onEditClick = {
                        selectedToDo = todo
                        showEditDialog = true
                    },
                    onDeleteClick = {
                        try {
                            viewModel.deleteToDoById(todo.id)
                        } catch (e: Exception) {
                            android.util.Log.e("DashboardScreen", "Error deleting ToDo", e)
                        }
                    },
                    onCompleteClick = {
                        try {
                            viewModel.markToDoAsCompleted(todo)
                        } catch (e: Exception) {
                            android.util.Log.e("DashboardScreen", "Error marking ToDo as completed", e)
                        }
                    },
                    onReopenClick = {
                        try {
                            viewModel.reopenToDo(todo)
                        } catch (e: Exception) {
                            android.util.Log.e("DashboardScreen", "Error reopening ToDo", e)
                        }
                    }
                )
            }
        }
    }

    if (showEditDialog) {
        EditToDoDialog(
            todo = selectedToDo,
            onDismiss = { showEditDialog = false },
            onSave = { todo ->
                try {
                    viewModel.addOrUpdateToDo(todo)
                    showEditDialog = false
                } catch (e: Exception) {
                    android.util.Log.e("DashboardScreen", "Error saving ToDo", e)
                }
            },
            onDelete = { todo ->
                try {
                    viewModel.deleteToDoById(todo.id)
                    showEditDialog = false
                } catch (e: Exception) {
                    android.util.Log.e("DashboardScreen", "Error deleting ToDo", e)
                }
            }
        )
    }
}

/**
 * Displays a single To Do in a card layout with options to edit, delete, mark as complete, or reopen
 *
 * @param todo the [ToDoDataClass] object representing a single ToDo task
 * @param onEditClick the callback to handle the edit action
 * @param onDeleteClick the callback to handle the delete action
 * @param onCompleteClick the callback to handle marking the task as completed
 * @param onReopenClick the callback to handle reopening the task
 */
@Composable
fun TodoCard(
    todo: ToDoDataClass,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCompleteClick: () -> Unit,
    onReopenClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onEditClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Due: ${todo.dueDate}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Priority: ${todo.priority}",
                    style = MaterialTheme.typography.bodySmall,
                    color = when (todo.priority) {
                        "high" -> Color.Red
                        "medium" -> Color.Yellow
                        "low" -> Color.Green
                        else -> Color.Gray
                    }
                )
            }
            Row(modifier = Modifier.wrapContentWidth()) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit ToDo")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete ToDo")
                }
                if (todo.status == "completed") {
                    IconButton(onClick = onReopenClick) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Reopen ToDo", tint = Color.Gray)
                    }
                } else {
                    IconButton(onClick = onCompleteClick) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Mark as Completed", tint = Color.Blue)
                    }
                }
            }
        }
    }
}
