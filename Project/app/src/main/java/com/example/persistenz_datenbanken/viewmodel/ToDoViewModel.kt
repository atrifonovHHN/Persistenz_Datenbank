package com.example.persistenz_datenbanken.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.persistenz_datenbanken.database.controller.ToDoController
import com.example.persistenz_datenbanken.database.dataclass.ToDoDataClass

/**
 * ViewModel for managing To Do items in the application
 * It handles all operations related to To Do items such as loading, adding, updating, deleting, and changing status
 *
 * @param application the application context
 */
class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    // Controller for interacting with the To Do database
    private val todoController = ToDoController(application)

    // LiveData for holding the list of To Do items
    private val _todos = MutableLiveData<List<ToDoDataClass>>()
    val todos: LiveData<List<ToDoDataClass>> get() = _todos

    init {
        loadTodos()
    }

    /**
     * Loads all the To Do items from the database
     */
    private fun loadTodos() {
        try {
            _todos.value = todoController.getAllToDos()
        } catch (e: Exception) {
            android.util.Log.e("ToDoViewModel", "Error loading ToDos", e)
        }
    }

    /**
     * Adds a new To Do or updates an existing one based on the provided To Do data
     *
     * @param todo the To Do data to be added or updated
     */
    fun addOrUpdateToDo(todo: ToDoDataClass) {
        val context = getApplication<Application>().applicationContext
        try {
            if (todo.id == 0) {
                todoController.addToDo(todo)  // Add a new To Do if it has no ID
            } else {
                todoController.updateToDo(todo)  // Update existing To Do if it has an ID
            }
            loadTodos()  // Reload the list of ToDos after adding or updating
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to save ToDo", Toast.LENGTH_SHORT).show()
            android.util.Log.e("ToDoViewModel", "Error saving ToDo", e)
        }
    }

    /**
     * Deletes a To Do item based on the provided ID
     *
     * @param id the ID of the To Do item to be deleted
     */
    fun deleteToDoById(id: Int) {
        val context = getApplication<Application>().applicationContext
        try {
            todoController.deleteToDo(id)
            loadTodos()  // Reload the list of ToDos after deletion
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to delete ToDo", Toast.LENGTH_SHORT).show()
            android.util.Log.e("ToDoViewModel", "Error deleting ToDo", e)
        }
    }

    /**
     * Marks a To Do item as completed by updating its status to "completed"
     *
     * @param todo the To Do item to be marked as completed
     */
    fun markToDoAsCompleted(todo: ToDoDataClass) {
        val context = getApplication<Application>().applicationContext
        try {
            val updatedToDo = todo.copy(status = "completed")
            todoController.updateToDo(updatedToDo)
            loadTodos()  // Reload the list of ToDos after marking as completed
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to mark ToDo as completed", Toast.LENGTH_SHORT).show()
            android.util.Log.e("ToDoViewModel", "Error marking ToDo as completed", e)
        }
    }

    /**
     * Reopens a completed To Do item by updating its status back to "open"
     *
     * @param todo the to do item to be reopened
     */
    fun reopenToDo(todo: ToDoDataClass) {
        val context = getApplication<Application>().applicationContext
        try {
            val updatedToDo = todo.copy(status = "open")
            todoController.updateToDo(updatedToDo)
            loadTodos()  // Reload the list of ToDos after reopening
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to reopen ToDo", Toast.LENGTH_SHORT).show()
            android.util.Log.e("ToDoViewModel", "Error reopening ToDo", e)
        }
    }
}
