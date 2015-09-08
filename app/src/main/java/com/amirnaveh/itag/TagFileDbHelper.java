package com.amirnaveh.itag;

/**
 * Tag Photo DB Helper
 * This is the helper for managing the DB of tags for the files
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * This class is a helper class for handling the DB for the To-Do List Manager app
 */
public class TagFileDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    protected static final String DATABASE_NAME = "tags_db"; // Database name
    protected static final String TABLE_NAME = "tags_table"; // Table name
    // Table columns names
    protected static final String COL1 = BaseColumns._ID;
    protected static final String COL2 = "file_name"; // full file name (including path)
    //    private static final String COL3 = "path"; // file path
    protected static final String COL3 = "tags";

    public TagFileDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT)";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Drop older table if existed
        onCreate(db); // Create tables again

    }

}
