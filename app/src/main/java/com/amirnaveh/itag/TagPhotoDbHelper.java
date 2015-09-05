package com.amirnaveh.itag;

/**
 * Tag Photo DB Helper
 * This is the helper for managing the DB of keywords for the files
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * This class is a helper class for handling the DB for the To-Do List Manager app
 */
public class TagPhotoDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tags_db"; // Database name
    private static final String TABLE_NAME = "tags_table"; // Table name
    // Table columns names
    private static final String COL1 = BaseColumns._ID;
    private static final String COL2 = "file_name"; // full file name (including path)
    //    private static final String COL3 = "path"; // file path
    private static final String COL3 = "keyword1";
    private static final String COL4 = "keyword2";
    private static final String COL5 = "keyword3";
    private static final String COL6 = "keyword4";
    private static final String COL7 = "keyword5";
    private static final String COL8 = "keyword6";
    private static final String COL9 = "keyword7";
    private static final String COL10 = "keyword8";
    private static final String COL11 = "keyword9";
    private static final String COL12 = "keyword10";

//    private static final int counter = 0;

    public TagPhotoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT, " +
                COL7 + " TEXT, " +
                COL8 + " TEXT, " +
                COL9 + " TEXT, " +
                COL10 + " TEXT, " +
                COL11 + " TEXT, " +
                COL12 + " TEXT)";


        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Drop older table if existed
        onCreate(db); // Create tables again

    }

    public long addTags(String fileName, String[] keywords) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2, fileName); // task name
        for (int i = 0; i < keywords.length; i++) { // Add keywords to corresponding column
            values.put("keyword" + Integer.toString((i + 1)), keywords[i]);
        }

        long id = db.insert(TABLE_NAME, null, values); // Inserting row to db, getting the row id

        db.close(); // Closing database connection

        return id;

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * from " + TABLE_NAME, null);

        db.close();

        return res;
    }

    /**
     * This method updates the keywords for the file passed as a parameter from scratch
     *
     * @param fileName - the file name
     * @param keywords - the keywords for the file
     * @return true if update went successfuly, false if there was a problem and more than one row
     * was changed
     */
    public boolean updateTags(String fileName, String[] keywords) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2, fileName); // task name
        for (int i = 0; i < keywords.length; i++) { // Add keywords to corresponding column
            values.put("keyword" + Integer.toString((i + 1)), keywords[i]);
        }

        int rowsAffected = db.update(TABLE_NAME, values, "file_name = ?", new String[]{fileName});

        db.close();

        return (rowsAffected == 1);

    }

    public boolean deleteFile(String fileName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME, "file_name = ?", new String[]{fileName});

        db.close();

        return (rowsAffected == 1);
    }

}
