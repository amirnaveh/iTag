package com.amirnaveh.itag;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by quickode037 on 8/13/15.
 */
public class GridViewActivity extends Activity {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ArrayList<String> filePaths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        filePaths = getIntent().getStringArrayListExtra("fileNames");

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item, getData());
        gridView.setAdapter(gridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);

                Intent intent = new Intent("com.amirnaveh.itag.ImageWithTagsActivity");
                intent.putExtra("path", item.getPath());
                intent.putExtra("image", item.getImage());
                intent.putStringArrayListExtra("tags", item.getTags());

                GridViewActivity.this.startActivity(intent);
            }
        });
    }

    // Prepare some dummy data for gridview
    private ArrayList<String> getData() {
        return this.filePaths;
    }
}

