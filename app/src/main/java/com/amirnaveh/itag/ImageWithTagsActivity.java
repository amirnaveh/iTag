package com.amirnaveh.itag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
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

//    private int widestColumn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_tags);

        horizontalScrollViewTags = (HorizontalScrollView) findViewById(R.id.HorizontalScrollView_Tags);
//        tagGridViewAdapter = new TagGridViewAdapter(this, R.layout.tag_grid_view_item, getData());
//        gridViewTags.setAdapter(tagGridViewAdapter);
        linearLayoutInHorizontal = (LinearLayout) findViewById(R.id.LinearLayout_In_Horizontal_Layout);

        String title = getIntent().getStringExtra("title");
        Bitmap bitmap = getIntent().getParcelableExtra("image");

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);



//        linearLayout.setDividerDrawable(getResources().getDrawable(R.drawable.divider));
        for (int i = 0; i < getData().size(); i++) {

            final TextView textView = new TextView(this);

            textView.setTextColor(Color.WHITE);
            textView.setTextSize(20);
            textView.setPadding(10,0,10,0);
            textView.setTag(i);
            final int j = i;
            textView.setText("#" + (String) getData().get(i));


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(getData().get(j));
                }
            });

            linearLayoutInHorizontal.addView(textView);
        }



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

    public ArrayList getData() {
        final ArrayList tags = getIntent().getExtras().getStringArrayList("tags");
        return tags;
    }
}
