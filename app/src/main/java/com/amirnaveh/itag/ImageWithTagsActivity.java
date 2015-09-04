package com.amirnaveh.itag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mattan on 01/09/2015.
 */
public class ImageWithTagsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_tags);

        String title = getIntent().getStringExtra("title");
        Bitmap bitmap = getIntent().getParcelableExtra("image");

        GridView titleTextView = (GridView) findViewById(R.id.tags);
        titleTextView.;

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);

        ArrayList tags = getIntent().getStringArrayListExtra("tags");

//        if(tags.isEmpty()){
//            titleTextView.setVisibility(View.INVISIBLE);
//
//        }
        Button addTagButton = (Button) findViewById(R.id.add_tag_button);

        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTagActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

}
