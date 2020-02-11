package com.example.todo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "MyDB"
val TABLE_NAME = "ToDo"
val COL_TILTLE = "title"
val COL_ISCOMPLETE = "isComplete"
val COL_CONTENT = "content"
val COL_ID = "id"


class DataBaseHander(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null,1){
    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME +"(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TILTLE + " TEXT," +
                COL_CONTENT + " TEXT," +
                COL_ISCOMPLETE + " BIT" + ")"

        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(p0)
    }

    fun getAllTodo(): MutableList<DetailTodo>{
        var list: MutableList<DetailTodo> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result =db.rawQuery(query,null)

        if (result.moveToFirst()){
            do {
                var todo = DetailTodo()
                todo.id = result.getInt(result.getColumnIndex(COL_ID))
                todo.title = result.getString(result.getColumnIndex(COL_TILTLE))
                todo.content = result.getString(result.getColumnIndex(COL_CONTENT))

                if (result.getInt(result.getColumnIndex(COL_ISCOMPLETE))== 0){
                    todo.isComplete = false
                }else {
                    todo.isComplete = true
                }
                list.add(todo)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }

    fun insertData(todo: DetailTodo){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_TILTLE, todo.title)
        cv.put(COL_CONTENT, todo.content)
        if (todo.isComplete == false){
            cv.put(COL_ISCOMPLETE, 0)
        }else
            cv.put(COL_ISCOMPLETE, 1)

        var result = db.insert(TABLE_NAME,null,cv)
        if (result == -1.toLong()) {
            Toast.makeText(context, "Failed", Toast.LENGTH_LONG).show()
        }else
            Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
    }

    fun deleteData(todo: DetailTodo){
        val db = this.writableDatabase

        db.delete(TABLE_NAME, "$COL_ID=?", arrayOf(todo.id.toString()))
        db.close()
    }

    fun updateData(todo: DetailTodo){
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_TILTLE, todo.title)
        cv.put(COL_CONTENT,todo.content)
        if (todo.isComplete == false){
            cv.put(COL_ISCOMPLETE, 0)
        }else
            cv.put(COL_ISCOMPLETE, 1)

        db.update(TABLE_NAME,cv,"$COL_ID =?", arrayOf(todo.id.toString()))
    }

    fun getOneData(string: String): DetailTodo?{
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_TILTLE LIKE '$string'"
        var todo = DetailTodo()

        val cursor = db.query(TABLE_NAME, arrayOf(COL_ID, COL_TILTLE, COL_CONTENT, COL_ISCOMPLETE), COL_TILTLE + "=?",
            arrayOf(string),null,null,null,null)
        if (cursor != null){
            cursor.moveToFirst()
        }
        todo.id = cursor.getInt(cursor.getColumnIndex(COL_ID))
        todo.title = cursor.getString(cursor.getColumnIndex(COL_TILTLE))
        todo.content = cursor.getString(cursor.getColumnIndex(COL_CONTENT))
        if (cursor.getInt(cursor.getColumnIndex(COL_ISCOMPLETE))== 0){
            todo.isComplete = false
        }else {
            todo.isComplete = true
        }
        return todo
    }
}