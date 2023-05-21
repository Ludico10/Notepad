package com.example.try2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID


class SQLiteList(context: Context) : SQLiteOpenHelper(context, "NOTEPAD", null, 1), StorageList {
    private val tableName = "NOTES"
    private val columnHandle = "HANDLE"
    private val columnProto = "PROTOTYPE"
    private val columnText = "FULL_TEXT"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE $tableName ($_ID INTEGER PRIMARY KEY AUTOINCREMENT, $columnHandle TEXT, $columnProto TEXT, $columnText TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $tableName")
        onCreate(db)
    }

    override fun allFiles() : ArrayList<ListItem> {
        val sql = "select * from $tableName"
        val db = this.readableDatabase
        val list = ArrayList<ListItem>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(0).toInt()
                val handle = cursor.getString(1)
                val proto = cursor.getString(2)
                list.add(ListItem(id, handle, proto))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    override fun searchFilesByText(sub : String) : ArrayList<ListItem>{
        val sql = "select * from $tableName"
        val db = this.readableDatabase
        val list = ArrayList<ListItem>()
        val cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                val text = cursor.getString(3)
                val handle = cursor.getString(1)
                if (handle.contains(sub) || text.contains(sub)) {
                    val id = cursor.getString(0).toInt()
                    val proto = cursor.getString(2)
                    list.add(ListItem(id, handle, proto))
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    override fun searchFileByID(index: Int): ListItem? {
        val sql = "select * from $tableName WHERE $_ID = $index"
        val db = this.readableDatabase
        val cursor = db.rawQuery(sql, null)
        var res : ListItem? = null
        if (cursor.moveToFirst()) {
            val text = cursor.getString(3)
            val handle = cursor.getString(1)
            res = ListItem(index, handle, text)
        }
        cursor.close()
        return res
    }

    override fun saveFile(index : Int, title : String, text : String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(columnHandle, title)
        values.put(columnProto, text.substringBefore('\n'))
        values.put(columnText, text)
        val cnt = db.update(tableName, values, "$_ID = ?", arrayOf(index.toString()))
        if (cnt == 0)
            db.insert(tableName, null, values)
    }

    override fun deleteFile(el : ListItem) {
        val db = this.writableDatabase
        db.delete(tableName, "$_ID	= ?", arrayOf(el.id.toString()))
    }

}