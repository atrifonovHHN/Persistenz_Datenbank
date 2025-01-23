package com.example.persistenz_datenbanken.database.controller

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.persistenz_datenbanken.database.DbHelper
import com.example.persistenz_datenbanken.database.dataclass.ToDoDataClass

/**
 * Controller class for managing to do data operations in the database
 *
 * This class provides methods for adding, updating, deleting, and retrieving To Do items
 * from the SQLite database. It interacts with the database using the [DbHelper] class
 * and returns results as [ToDoDataClass] objects
 *
 * @property context the context used to initialize the [DbHelper] instance
 */
class ToDoController(context: Context) {
    private val dbHelper = DbHelper(context)

    /**
     * Adds a new To DO item to the database
     *
     * @param todo the [ToDoDataClass] object containing the data to be inserted
     * @return true if the insertion was successful, false otherwise.
     */
    fun addToDo(todo: ToDoDataClass): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("priority", todo.priority)
                put("description", todo.description)
                put("status", todo.status)
                put("dueDate", todo.dueDate)
            }
            val result = db.insert("todos", null, values)
            result != -1L
        } catch (e: Exception) {
            Log.e("ToDoController", "Insert failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Updates an existing To Do item in the database
     *
     * @param todo the [ToDoDataClass] object containing the updated data
     * @return true if the update was successful, false otherwise
     */
    fun updateToDo(todo: ToDoDataClass): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = ContentValues().apply {
                put("name", todo.name)
                put("priority", todo.priority)
                put("description", todo.description)
                put("status", todo.status)
                put("dueDate", todo.dueDate)
            }
            val result = db.update("todos", values, "id = ?", arrayOf(todo.id.toString()))
            result > 0
        } catch (e: Exception) {
            Log.e("ToDoController", "Update failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Deletes a To Do item from the database by its ID
     *
     * @param id the ID of the To Do item to be deleted
     * @return true if the deletion was successful, false otherwise
     */
    fun deleteToDo(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val result = db.delete("todos", "id = ?", arrayOf(id.toString()))
            result > 0
        } catch (e: Exception) {
            Log.e("ToDoController", "Delete failed", e)
            false
        } finally {
            db.close()
        }
    }

    /**
     * Retrieves all To Do items from the database
     *
     * @return a list of [ToDoDataClass] objects representing all the To Do items
     */
    fun getAllToDos(): List<ToDoDataClass> {
        val db = dbHelper.readableDatabase
        val todos = mutableListOf<ToDoDataClass>()
        val cursor: Cursor? = db.rawQuery("SELECT * FROM todos", null)
        cursor?.use {
            try {
                while (it.moveToNext()) {
                    val id = it.getInt(it.getColumnIndexOrThrow("id"))
                    val name = it.getString(it.getColumnIndexOrThrow("name"))
                    val priority = it.getString(it.getColumnIndexOrThrow("priority"))
                    val dueDate = it.getString(it.getColumnIndexOrThrow("dueDate"))
                    val description = it.getString(it.getColumnIndexOrThrow("description"))
                    val status = it.getString(it.getColumnIndexOrThrow("status"))

                    todos.add(
                        ToDoDataClass(id, name, priority, description, status, dueDate)
                    )
                }
            } catch (e: Exception) {
                Log.e("ToDoController", "Error reading from cursor", e)
            }
        } ?: Log.e("ToDoController", "Query failed")
        db.close()
        return todos
    }
}
