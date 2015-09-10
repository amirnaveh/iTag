package com.amirnaveh.itag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mattan on 01/09/2015.
 *
 */
public class ImageWithTagsActivity extends AppCompatActivity {

    private HorizontalScrollView horizontalScrollViewTags;
    private LinearLayout linearLayoutInHorizontal;
//    private TagGridViewAdapter tagGridViewAdapter;
    private ArrayList data;
    private String path;

//    private int widestColumn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_tags);

        horizontalScrollViewTags = (HorizontalScrollView) findViewById(R.id.HorizontalScrollView_Tags);
//        tagGridViewAdapter = new TagGridViewAdapter(this, R.layout.tag_grid_view_item, getData());
//        gridViewTags.setAdapter(tagGridViewAdapter);
        linearLayoutInHorizontal = (LinearLayout) findViewById(R.id.LinearLayout_In_Horizontal_Layout);

        this.path = getIntent().getStringExtra("path");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;


        int height = options.outHeight;
        int width = options.outWidth;
        int sampleSize = 1;
        while (height/sampleSize > 4096 || width/sampleSize > 4096) {
            sampleSize *= 2;
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);


//        linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
        updateTags();


//        if(tags.isEmpty()){
//            title.setVisibility(View.INVISIBLE);
//
//        }
        Button addTagButton = (Button) findViewById(R.id.add_tag_button);

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callAddTag();
            }
        });
    }

    private void updateTags() {

        linearLayoutInHorizontal.removeAllViews();

        String imageTags = TagFileDb.getInstance(ImageWithTagsActivity.this).getTagsForFile(path);
        if (imageTags==null) {
            return;
        }
        String[] splitTags = imageTags.split(",");
        for (int i = 0; i<splitTags.length; i++) {

            final TextView textView = new TextView(this);

            textView.setTextColor(Color.WHITE);
            textView.setTextSize(20);
            textView.setPadding(10, 0, 10, 0);
            textView.setText("#" + splitTags[i] + " ");

            textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), DeleteTagActivity.class);
                    intent.putExtra("path", path);
                    String tag = textView.getText().toString().replaceAll("#", "");
                    tag = tag.replaceAll("\\s+", "");
                    intent.putExtra("tagToDelete", tag);
                    startActivityForResult(intent, Constants.DELETE_TAG_REQUEST_CODE);
                    return true;
                }
            });

            linearLayoutInHorizontal.addView(textView);
        }
    }

    private void callAddTag() {
        Intent intent = new Intent(getApplicationContext(), AddTagActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, Constants.ADD_TAG_REQUEST_CODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.image_add_tags:
                callAddTag();
                break;
            case R.id.image_delete_tags:
//                TODO

            default:
                return super.onOptionsItemSelected(item);
        }

        updateTags();
        return true;
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != resultCode) {
            return;
        }

        TagFileDb db = TagFileDb.getInstance(this);

        switch (resultCode) {
            case Constants.ADD_TAG_REQUEST_CODE:
                db.addTags(path, data.getExtras().getString(Constants.KEY_ADD_TAGS));
                break;

            case Constants.DELETE_TAG_REQUEST_CODE:
                db.deleteTags(path, data.getExtras().getString(Constants.KEY_DELETE_TAGS));
                break;

            case Constants.UPDATE_TAG_REQUEST_CODE:
                break;

            default:
                break;
        }

        updateTags();


    }

}
