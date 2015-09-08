package com.amirnaveh.itag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;


public class TagActivity extends Activity {

    private Button btnSearch;
    private Button btnViewAll;
    private EditText editTextTagsToSearch;
    protected TagFileDb db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_tag);

        db = new TagFileDb(this);

        btnSearch = (Button) findViewById(R.id.button_search);
        btnViewAll = (Button) findViewById(R.id.button_viewAll);

        editTextTagsToSearch = (EditText)findViewById(R.id.EditText_Tags_To_Search);
//        db.getAllFiles();
        this.search();
        this.showAll();
    }

    private void showAll() {
        btnViewAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] allFileNames = db.getAllFiles();

                Intent intent = new Intent("com.amirnaveh.itag.GridViewActivity");
                intent.putStringArrayListExtra("fileNames", new ArrayList<String>(Arrays.asList(allFileNames)));

                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_i_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void search() {
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tagsToSearchStr = editTextTagsToSearch.getText().toString();
                String[] tagsToSearchArr = tagsToSearchStr.split(",");
                ArrayList fileNames = db.getFilesWithTag(tagsToSearchArr);

                Intent intent = new Intent("com.amirnaveh.itag.GridViewActivity");
                if(!fileNames.isEmpty()) {
                    intent.putStringArrayListExtra("fileNames", fileNames);
                }
                startActivity(intent);
            }
        });
    }




}
