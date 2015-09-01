package com.amirnaveh.itag;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mattan on 01/09/2015.
 */
public class ImageWithTagsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_tags);

        String title = getIntent().getStringExtra("title");
        ArrayList tags = getIntent().getStringArrayListExtra("tags");
        Bitmap bitmap = getIntent().getParcelableExtra("image");

        TextView titleTxtView = (TextView)findViewById(R.id.title);
        titleTxtView.setText(title);

        ImageView imgView = (ImageView)findViewById(R.id.image);
        imgView.setImageBitmap(bitmap);
    }

}
