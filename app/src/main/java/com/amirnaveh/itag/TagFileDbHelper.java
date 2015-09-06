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
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * This class is a helper class for handling the DB for the To-Do List Manager app
 */
public class TagFileDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tags_db"; // Database name
    private static final String TABLE_NAME = "tags_table"; // Table name
    // Table columns names
    private static final String COL1 = BaseColumns._ID;
    private static final String COL2 = "file_name"; // full file name (including path)
    //    private static final String COL3 = "path"; // file path
    private static final String COL3 = "keywords";

    public TagFileDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        addFilesToDb(findPhotos(context));
//        addFilesToDb(findVideos(context)); TODO addVideosToDb
//        addFilesToDb(findFiles(context)); TODO addFilesToDb

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE " + TABLE_NAME + " ( " +
                COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 + " TEXT, " +
                COL3 + " TEXT)";

        db.execSQL(sql);

    }

    private String[] findPhotos(Context context) {
        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];

        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i]= cursor.getString(dataColumnIndex);
        }

        return arrPath;

    }

    private void addFilesToDb(String[] files) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        for (String file : files) {
            values.put(COL2, file); // Add the file path
            values.put(COL3, ""); // Add empty keyword
            db.insert(TABLE_NAME, null, values); // Inserting row to db, getting the row id
        }

        db.close(); // Closing database connection

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); // Drop older table if existed
        onCreate(db); // Create tables again

    }

//    TODO REMOVE IF INDEED UNNECESSARY
//    public long addTags(String fileName, String keywords) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(COL2, fileName); // task name
//        for (int i = 0; i < keywords.length; i++) { // Add keywords to corresponding column
//            values.put("keyword" + Integer.toString((i + 1)), keywords[i]);
//        }
//
//        long id = db.insert(TABLE_NAME, null, values); // Inserting row to db, getting the row id
//
//        db.close(); // Closing database connection
//
//        return id;
//
//    }

    public String[] getAllFiles() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int numFiles = res.getCount();

        if (numFiles == 0) {
            return null;
        }

        String[] paths = new String[numFiles];

        for (int i=0; i<numFiles && res.moveToNext(); i++) {
            paths[i] = res.getString(1); // TODO fix a variable name instead of 1
        }

        db.close();
        res.close();

        return paths;
    }

    /**
     * This method updates the keywords for the file passed as a parameter from scratch
     *
     * @param fileName - the file name
     * @param keywords - the keywords for the file
     * @return true if update went successfuly, false if there was a problem and more than one row
     * was changed
     */
    public boolean updateTags(String fileName, String keywords) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2, fileName); // task name
        values.put(COL3, keywords);

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

    /**
     * This method returns the tags for a specific file
     * @param fileName - The file to find the tags for
     * @return a String array of all the tags for the specific file
     */
    public String getTags(String fileName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor row = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL2 + " = ?",
                new String[]{fileName});

        if (row.getCount() == 0) {
            return null;
        }

        row.moveToNext();

        db.close();

        return row.getString(2);

    }


    public ArrayList<String> getFilesWithTag (String[] keywords) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL3 + " = ?";

        Cursor row = db.rawQuery(sql, keywords);

        if (row.getCount() == 0) {
            return null;
        }

        ArrayList<String> files = new ArrayList<>();

        while (row.moveToNext()) {
            files.add(row.getString(1));  // TODO fix variable name
        }

        db.close();
        row.close();

        return files;

    }

}
