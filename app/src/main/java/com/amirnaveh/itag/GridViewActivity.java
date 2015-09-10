package com.amirnaveh.itag;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * Created by quickode037 on 8/13/15.
 */
public class GridViewActivity extends AppCompatActivity {
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
                String path = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(GridViewActivity.this, ImageWithTagsActivity.class);
                intent.putExtra("path", path);
                String tags = TagFileDb.getInstance(GridViewActivity.this).getTagsForFile(path);
                intent.putExtra("tags", tags);

//                intent.putStringArrayListExtra("tags", );

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grid_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.grid_add_tags:
//                TODO
                return true;
            case R.id.grid_delete_tags:
//                TODO

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Prepare some dummy data for gridview
    private ArrayList<String> getData() {
        return this.filePaths;
    }
}

