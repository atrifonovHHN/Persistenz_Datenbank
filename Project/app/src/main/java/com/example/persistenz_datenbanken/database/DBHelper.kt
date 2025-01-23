package com.example.persistenz_datenbanken.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException

/**
 * Helper class for managing the SQLite database
 *
 * This class extends [SQLiteOpenHelper] to manage the creation, upgrade, and opening of the database
 * The database is copied from the assets folder if it does not exist
 *
 * @property context the context used to access the assets and database
 */
class DbHelper(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    /**
     * Called when the database is created for the first time
     * This method remains empty since we are using an existing database from assets
     */
    override fun onCreate(db: SQLiteDatabase?) {
        try {
            // The method remains empty as we are using an existing database from assets
        } catch (e: Exception) {
            android.util.Log.e("DbHelper", "Error in onCreate", e)
        }
    }

    /**
     * Called when the database needs to be upgraded
     * It deletes the existing database and copies a new one from the assets
     *
     * @param db the database
     * @param oldVersion the old version number
     * @param newVersion the new version number
     */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try {
            context.deleteDatabase(DATABASE_NAME)
            copyDatabaseFromAssets()
        } catch (e: Exception) {
            android.util.Log.e("DbHelper", "Error in onUpgrade", e)
        }
    }

    /**
     * Gets a readable instance of the database
     * Copies the database from assets if necessary
     *
     * @return a readable [SQLiteDatabase] instance
     * @throws RuntimeException if there is an error opening the database
     */
    override fun getReadableDatabase(): SQLiteDatabase {
        return try {
            copyDatabaseFromAssets()
            super.getReadableDatabase()
        } catch (e: Exception) {
            android.util.Log.e("DbHelper", "Error in getReadableDatabase", e)
            throw RuntimeException("Error opening readable database", e)
        }
    }

    /**
     * Gets a writable instance of the database
     * Copies the database from assets if necessary
     *
     * @return a writable [SQLiteDatabase] instance
     * @throws RuntimeException if there is an error opening the database
     */
    override fun getWritableDatabase(): SQLiteDatabase {
        return try {
            copyDatabaseFromAssets()
            super.getWritableDatabase()
        } catch (e: Exception) {
            android.util.Log.e("DbHelper", "Error in getWritableDatabase", e)
            throw RuntimeException("Error opening writable database", e)
        }
    }

    /**
     * Copies the database from assets to the application's database directory
     * This method ensures that the database is available for use
     */
    private fun copyDatabaseFromAssets() {
        val dbPath = context.getDatabasePath(DATABASE_NAME)
        if (!dbPath.exists()) {
            try {
                context.assets.open(DATABASE_NAME).use { inputStream ->
                    FileOutputStream(dbPath).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                android.util.Log.d("DbHelper", "Database copied successfully to: ${dbPath.absolutePath}")
                android.util.Log.d("DbHelper", "Database size: ${dbPath.length()} bytes")
            } catch (e: IOException) {
                android.util.Log.e("DbHelper", "Error copying database", e)
                throw RuntimeException("Error copying database from assets", e)
            }
        }
    }

    companion object {
        const val DATABASE_NAME = "todo.db"
        const val DATABASE_VERSION = 1
    }
}
