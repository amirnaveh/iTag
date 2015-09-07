package com.amirnaveh.itag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * This is the TagFile DB class
 * Created by amir__000 on 07/09/2015.
 */
public class TagFileDb {

    private SQLiteDatabase db;
    private TagFileDbHelper dbHelper;

    private static final String SELECT_ALL = "SELECT * FROM " + TagFileDbHelper.TABLE_NAME;
    private static final int PATH_INDEX_DB = 1;
    private static final int TAGS_INDEX_DB = 2;


    public TagFileDb(Context context) {
        this.dbHelper = new TagFileDbHelper(context);
        openRead();
        Cursor mCursor = db.rawQuery(SELECT_ALL, null);
        close();

        if (!mCursor.moveToFirst()) {
            addFilesToDb(findPhotos(context));
//            addFilesToDb(findVideos(context)); TODO addVideosToDb
//            adFilesToDb(findFiles(context)); TODO addFilesToDb
        }

    }

    public void open() {
        this.db = dbHelper.getWritableDatabase();
    }

    public void openRead() {
        this.db = dbHelper.getReadableDatabase();
    }

    public void close() {
        this.db.close();
    }


    private String[] findPhotos(Context context) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
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
            arrPath[i] = cursor.getString(dataColumnIndex);
        }

        return arrPath;

    }

    private void addFilesToDb(String[] files) {

        open();

        ContentValues values = new ContentValues();

        for (String file : files) {
            values.put(TagFileDbHelper.COL2, file); // Add the file path
            values.put(TagFileDbHelper.COL3, ""); // Add empty keyword
            db.insert(TagFileDbHelper.TABLE_NAME, null, values); // Inserting row to db, getting the row id
        }

        db.close(); // Closing database connection

    }


    public String[] getAllFiles() {
        openRead();
        Cursor res = db.rawQuery(SELECT_ALL, null);
        int numFiles = res.getCount();

        if (numFiles == 0) {
            return null;
        }

        String[] paths = new String[numFiles];

        for (int i = 0; i < numFiles && res.moveToNext(); i++) {
            paths[i] = res.getString(PATH_INDEX_DB);
        }

        close();
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
        open();

        ContentValues values = new ContentValues();
        values.put(TagFileDbHelper.COL2, fileName); // task name
        values.put(TagFileDbHelper.COL3, keywords);

        int rowsAffected = db.update(TagFileDbHelper.TABLE_NAME, values, "file_name = ?",
                new String[]{fileName});

        close();

        return (rowsAffected == 1);

    }

    public boolean deleteFile(String fileName) {
        open();
        int rowsAffected = db.delete(TagFileDbHelper.TABLE_NAME, "file_name = ?",
                new String[]{fileName});

        close();

        return (rowsAffected == 1);
    }

    /**
     * This method returns the tags for a specific file
     *
     * @param fileName - The file to find the tags for
     * @return a String array of all the tags for the specific file
     */
    public String getTags(String fileName) {
        openRead();

        Cursor row = db.rawQuery("SELECT * FROM " + TagFileDbHelper.TABLE_NAME +
                " WHERE " + TagFileDbHelper.COL2 + " = ?", new String[]{fileName});

        if (row.getCount() == 0) {
            return null;
        }

        row.moveToNext();

        close();

        return row.getString(TAGS_INDEX_DB);

    }


    public ArrayList<String> getFilesWithTag(String[] keywords) {
        openRead();

        String sql = "SELECT * FROM " + TagFileDbHelper.TABLE_NAME +
                " WHERE " + TagFileDbHelper.COL3 + " = ?";

        Cursor row = db.rawQuery(sql, keywords);

        if (row.getCount() == 0) {
            return null;
        }

        ArrayList<String> files = new ArrayList<>();

        while (row.moveToNext()) {
            files.add(row.getString(PATH_INDEX_DB));
        }

        close();
        row.close();

        return files;

    }


}
