package com.amirnaveh.itag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

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
    private static final String TAG_SEPARATOR = ",";


    public TagFileDb(Context context) {
        this.dbHelper = new TagFileDbHelper(context);
        open();

        Cursor mCursor = db.rawQuery(SELECT_ALL, null);
        if (!mCursor.moveToFirst()) {
            addFilesToDb(findPhotos(context));
//            addFilesToDb(findVideos(context)); TODO addVideosToDb
//            adFilesToDb(findFiles(context)); TODO addFilesToDb
        }
        mCursor.close();

        cleanAllDeleted(this.getAllFiles());

        close();

    }

    private void cleanAllDeleted(String[] allFiles) {
        for (String filePath : allFiles) {
            File file = new File(filePath);
            if (!file.exists()) {
                deleteFileFromDb(filePath);
            }
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

        cursor.close();

        return arrPath;

    }

    private void addFilesToDb(String[] files) {

        open();

        ContentValues values = new ContentValues();

        for (String file : files) {
            values.put(TagFileDbHelper.COL2, file); // Add the file path
            values.put(TagFileDbHelper.COL3, ""); // Add empty tag
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
     * This method updates the tags for the file passed as a parameter from scratch
     *
     * @param fileName - the file name
     * @param newTags - the tags for the file
     * @return true if update went successfuly, false if there was a problem and more than one row
     * was changed
     */
    public boolean updateTags(String fileName, String newTags) {
        open();

        String currentTags = this.getTags(fileName);
        ArrayList<String> arrayCurrentTags = new ArrayList(Arrays.asList(currentTags.split(TAG_SEPARATOR)));

        String[] splitNewTags = newTags.split(TAG_SEPARATOR);

        for (int i=0; i<splitNewTags.length; i++) {
            if (!arrayCurrentTags.contains(splitNewTags[i])){
                arrayCurrentTags.add(splitNewTags[i]);
            }
        }

        String updatedTags = arrayCurrentTags.toString().replaceAll(" ","");

        ContentValues values = new ContentValues();
        values.put(TagFileDbHelper.COL2, fileName); // file name
        values.put(TagFileDbHelper.COL3, updatedTags); // file tags

        int rowsAffected = db.update(TagFileDbHelper.TABLE_NAME, values, "file_name = ?",
                new String[]{fileName});

        close();

        return (rowsAffected == 1);

    }

    public boolean deleteFileFromDb(String fileName) {
        open();
        int rowsAffected = db.delete(TagFileDbHelper.TABLE_NAME, "file_name = ?",
                new String[]{fileName});

        close();

        return (rowsAffected == 1);
    }

    /**
     * This method returns the tags for a specific file in a string
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
        String tags = row.getString(TAGS_INDEX_DB);

        row.close();
        close();

        return tags;

    }


    public String[] getFilesWithTag(String[] tags) {
        openRead();

        String sql = "SELECT * FROM " + TagFileDbHelper.TABLE_NAME +
                " WHERE " + TagFileDbHelper.COL3 + " = ?";

        Cursor row = db.rawQuery(sql, tags);

        int numOfFiles = row.getCount();

        if (numOfFiles == 0) {
            return null;
        }

        String[] files = new String[numOfFiles];

        for (int i=0; i<numOfFiles && row.moveToNext(); i++) {
            files[i]=row.getString(PATH_INDEX_DB);
        }

        close();
        row.close();

        return files;

    }


}
