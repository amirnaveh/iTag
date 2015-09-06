package com.amirnaveh.itag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class TagActivity extends Activity {

    private Button btnSearch;
    private Button btnViewAll;
    protected static TagFileDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_i_tag);

        db = new TagFileDbHelper(this);

        btnSearch = (Button) findViewById(R.id.button_search);
        btnViewAll = (Button) findViewById(R.id.button_viewAll);

        this.search();
        this.viewAll();
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

    public void search() {
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.amirnaveh.itag.GridViewActivity");
                startActivity(intent);
            }
        });
    }

    public void viewAll() {
        btnViewAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = db.getAllData();
                if (res.getCount() == 0) {
                    showMessage("Error", "Sorry, I found nothing :(");
                    return;
                }


                StringBuilder builder = new StringBuilder();
                while (res.moveToNext()) { // TODO fix ALL variable names where necessary (fixed values)
                    String name = "File name: " + res.getString(1) + "\n";
                    builder.append(name);
                    for (int i = 2; i < 12 && (!res.getString(i).isEmpty()); i++) { // TODO notice variables here as well
                        String keywordToAdd = "#" + res.getString(i) + ", ";
                        builder.append(keywordToAdd);
                    }
                }

                // TODO Currently it shows textual information of the photo, needs to change to gallery view
                showMessage("Data", builder.toString());


            }
        });

    }

    public void showMessage(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

}
