package com.amirnaveh.itag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Tag Activity Class
 * Created by Mattan on 02/09/2015.
 */


public class AddTagActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tag);

        Button btnAdd = (Button) findViewById(R.id.button_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText addedTags = (EditText) findViewById(R.id.edit_text_add_tag);

                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_ADD_TAGS, addedTags.getText().toString());

                setResult(Constants.ADD_TAG_REQUEST_CODE, intent);
                finish();
            }
        });
    }




}
