package com.amirnaveh.itag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This is the TagFile DB class
 * Created by amir__000 on 07/09/2015.
 */
public class TagFileDb extends SQLiteOpenHelper {

    private static TagFileDb dbInstance;

    private static final int DATABASE_VERSION = 1;

    protected static final String DATABASE_NAME = "tags_db"; // Database name
    protected static final String TABLE_NAME = "tags_table"; // Table name
    // Table columns names
    protected static final String COL1 = BaseColumns._ID;
    protected static final String COL2 = "file_name"; // full file name (including path)
    protected static final String COL3 = "tags";

    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final int PATH_INDEX_DB = 1;
    private static final int TAGS_INDEX_DB = 2;
    private static final String TAG_SEPARATOR = ",";


    public static synchronized TagFileDb getInstance (Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dbInstance == null) {
            dbInstance = new TagFileDb(context.getApplicationContext());
        }
        return dbInstance;
    }


    private TagFileDb (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor mCursor = db.rawQuery(SELECT_ALL, null);
        if (!mCursor.moveToFirst()) {
            addFilesToDb(findPhotos(context));
//            addFilesToDb(findVideos(context)); TODO addVideosToDb
//            adFilesToDb(findFiles(context)); TODO addFilesToDb
        }
        mCursor.close();

        cleanAllDeleted(getAllFiles());

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

    /**
     * This method checks that all of the files stored in the database still exist in the device's storage and if not deletes them
     * @param currentFiles - the files currently stored in the db
     */
    private void cleanAllDeleted(String[] currentFiles) {
        for (String filePath : currentFiles) {
            File file = new File(filePath);
            if (!file.exists()) {
                deleteFileFromDb(filePath);
            }
        }
    }

    /**
     * Deletes a file from the DB
     * @param fileName the file to delete
     * @return true if successfully deleted file
     */
    public boolean deleteFileFromDb(String fileName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_NAME, COL2 + " = ?",
                new String[]{fileName});

        return (rowsAffected == 1);
    }

    /**
     * This method looks for all photos in the device
     * @param context - the current context
     * @return a string array containing the paths for all photos in the database
     */
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

    /**
     *
     * @param files
     */
    private void addFilesToDb(String[] files) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        for (String file : files) {
            values.put(COL2, file); // Add the file path
            values.put(COL3, ""); // Add empty tag
            db.insert(TABLE_NAME, null, values); // Inserting row to db, getting the row id
        }

    }


    /**
     * This method updates the tags for the file passed as a parameter from scratch
     *
     * @param fileName - the file name
     * @param newTags - the tags for the file
     * @return true if update went successfuly, false if there was a problem and more than one row
     * was changed
     */
    public boolean addTags(String fileName, String newTags) {
        SQLiteDatabase db = this.getWritableDatabase();

        String currentTags = this.getTagsForFile(fileName);
        String updatedTags;

        if (currentTags.equals("")) {
            updatedTags = newTags;
        }
        else {
            ArrayList<String> arrayCurrentTags = new ArrayList(Arrays.asList(currentTags.split(TAG_SEPARATOR)));

            String[] splitNewTags = newTags.split(TAG_SEPARATOR);

            for (String newTag : splitNewTags) {
                if (!arrayCurrentTags.contains(newTag)) {
                    arrayCurrentTags.add(newTag);
                }
            }

            updatedTags = arrayCurrentTags.toString().replaceAll(" ","");
        }


        ContentValues values = new ContentValues();
        values.put(COL2, fileName); // file name
        values.put(COL3, updatedTags); // file tags

        int rowsAffected = db.update(TABLE_NAME, values, (COL2 + " = ?"), new String[]{fileName});

        return (rowsAffected == 1);

    }


    /**
     * This method returns the tags for a specific file in a string
     *
     * @param fileName - The file to find the tags for
     * @return a String array of all the tags for the specific file
     */
    public String getTagsForFile(String fileName) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor row = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = ?", new String[]{fileName});

        if (row.getCount() == 0) {
            return null;
        }

        row.moveToNext();
        String tags = row.getString(TAGS_INDEX_DB);

        row.close();

        return tags;

    }

    /**
     * This method returns all of the files with a given tag
     * @param tags - the tags to search for
     * @return a string array with paths to all files with the given tag
     */
    public String[] getFilesWithTag(String[] tags) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL3 + " = ?";

        Cursor row = db.rawQuery(sql, tags);

        int numOfFiles = row.getCount();

        if (numOfFiles == 0) {
            return null;
        }

        String[] files = new String[numOfFiles];

        for (int i = 0; i < numOfFiles && row.moveToNext(); i++) {
            files[i] = row.getString(PATH_INDEX_DB);
        }

        row.close();

        return files;

    }

    public String[] getAllFiles() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery(SELECT_ALL, null);
        int numFiles = res.getCount();

        if (numFiles == 0) {
            return null;
        }

        String[] paths = new String[numFiles];

        for (int i = 0; i < numFiles && res.moveToNext(); i++) {
            paths[i] = res.getString(PATH_INDEX_DB);
        }

        res.close();

        return paths;
    }


}
