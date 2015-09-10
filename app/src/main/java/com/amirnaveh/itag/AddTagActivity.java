package com.amirnaveh.itag;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Tag Activity Class
 * Created by Mattan on 02/09/2015.
 */


public class AddTagActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tag);

        final Intent origIntent = getIntent();
        final String action = origIntent.getAction();
        final String type = origIntent.getType();

        Button btnAdd = (Button) findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                EditText addedTags = (EditText) findViewById(R.id.edit_text_add_tag);

                String newTags = addedTags.getText().toString();

                if (origIntent.ACTION_SEND.equals(action) && type!=null) {
                    if(type.startsWith("image/")) {
                        handleSendImage(origIntent);
                        finishAffinity();
                    }
                }
                else if (origIntent.ACTION_SEND_MULTIPLE.equals(action) && type!=null) {
                    if (type.startsWith("image/")) {
                        handleSendMultipleImages(origIntent); // Handle multiple images being sent
                    }
                }

                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_ADD_TAGS, newTags.replaceAll("\\s+",","));

                setResult(Constants.ADD_TAG_REQUEST_CODE, intent);
                finish();
            }
        });
    }

    private void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null ) {

            processSentImage(imageUri);

        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            Iterator<Uri> iter = imageUris.iterator();
            while (iter.hasNext()) {
                processSentImage(iter.next());
            }
        }
    }

    private void processSentImage(Uri image) {
        String path = getRealPathFromURI(AddTagActivity.this, image);
        TagFileDb db = TagFileDb.getInstance(AddTagActivity.this);

        String tags = ((EditText) findViewById(R.id.edit_text_add_tag)).getText().toString();

        db.addTags(path, tags.replaceAll("\\s+",","));

        return;
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


}
