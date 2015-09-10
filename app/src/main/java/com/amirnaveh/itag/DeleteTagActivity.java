package com.amirnaveh.itag;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Tag Activity Class
 * Created by Mattan on 02/09/2015.
 */


public class DeleteTagActivity extends Activity {

    private TextView deletedTag;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_tag);

        final String tagToDeleteText = getIntent().getStringExtra("tagToDelete");
        deletedTag = (TextView) findViewById(R.id.view_text_tag_delete);
        deletedTag.setText(tagToDeleteText);
        deletedTag.setTextColor(Color.WHITE);


        Button btnDelete = (Button) findViewById(R.id.button_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_DELETE_TAGS, tagToDeleteText);

                setResult(Constants.DELETE_TAG_REQUEST_CODE, intent);
                finish();
            }
        });
    }

}
