package com.amirnaveh.itag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Mattan on 01/09/2015.
 *
 */
public class ImageWithTagsActivity extends AppCompatActivity {

    private GridView gridViewTags;
    private TagGridViewAdapter tagGridViewAdapter;
    private ArrayList data;
    private int widestColumn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_with_tags);

        gridViewTags = (GridView) findViewById(R.id.tags_grid_view);
        tagGridViewAdapter = new TagGridViewAdapter(this, R.layout.tag_grid_view_item, getData());
        gridViewTags.setAdapter(tagGridViewAdapter);

        String title = getIntent().getStringExtra("title");
        Bitmap bitmap = getIntent().getParcelableExtra("image");

        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bitmap);

        if (tagGridViewAdapter.getData().isEmpty()){
            gridViewTags.setVisibility(View.GONE);
        }
        else {
            gridViewTags.setVisibility(View.VISIBLE);
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
