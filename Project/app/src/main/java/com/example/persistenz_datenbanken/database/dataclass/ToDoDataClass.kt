package com.example.persistenz_datenbanken.database.dataclass

/**
 * Data class representing a To Do item
 *
 * This class holds the details of a To Do item, including its unique ID, name, priority,
 * description, status, and due date
 *
 * @property id the unique ID of the To Do item
 * @property name the name of the To Do item
 * @property priority the priority of the To Do item ("high", "medium", "low")
 * @property description a brief description of the To Do item
 * @property status the current status of the To Do item ("open", "completed")
 * @property dueDate the due date of the To Do item
 */
data class ToDoDataClass(
    val id: Int = 0,
    val name: String,
    val priority: String,
    val description: String,
    val status: String,
    val dueDate: String
)
