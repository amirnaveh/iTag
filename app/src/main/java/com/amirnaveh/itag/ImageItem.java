package com.amirnaveh.itag;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by quickode037 on 8/13/15.
 */
public class ImageItem {
    private Bitmap image;
    private String title;
    private ArrayList<String> tags;

    public ImageItem(Bitmap image, String title) {
        super();
        this.image = image;
        this.title = title;
        this.tags = new ArrayList();
    }

    public void addTag(String tag){
        tags.add(tag);
    }

    public void removeTag(String tag){
        tags.remove(tag);
    }

    public ArrayList getTags(){
        return tags;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}