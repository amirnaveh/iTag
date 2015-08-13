package com.amirnaveh.itag;

/**
 * Created by amir__000 on 13/08/2015.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.List;
import java.util.ArrayList;

/**
 * This class is a helper class for handling the DB for the To-Do List Manager app
 */
public class TagDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tags_db"; // Database name
    private static final String TABLE_TAGS = "tags"; // tags table name

    // tasks Table columns names
    private static final String _id = BaseColumns._ID;
    private static final String IMG_NAME = "name";
    private static final String DUE = "due";
    private static final int counter = 0;

    private static final String SELECT_QUERY = "SELECT * FROM " + TABLE_TAGS;

    public TagDbHelper (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_TAGS +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IMG_NAME + " TEXT, " +
                DUE  + " TEXT)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_TAGS);
        // Create tables again
        onCreate(db);

    }

    public long addTask (Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoListDbHelper.task, task.getTask()); // task name
        values.put(due, task.getDueDate()); // task due date

        long id = db.insert(TABLE_TAGS, null, values); // Inserting row

        db.close(); // Closing database connection

        return id;

    }

    public void removeTask (Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        String delete = "DELETE FROM " + TABLE_TAGS + " WHERE " +
                _id + " = '" + task.getId() + "'";

        db.execSQL(delete);

        db.close(); // Closing database connection

    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(0));
                task.setTask(cursor.getString(1));
                task.setDueDate(cursor.getLong(2));

                // Adding task to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return task list
        return taskList;
    }

}
