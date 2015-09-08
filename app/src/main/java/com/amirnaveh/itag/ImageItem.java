package com.amirnaveh.itag;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by quickode037 on 8/13/15.
 */
public class ImageItem {
    private Bitmap image;
    private String imagePath;
    private ArrayList<String> tags;

    public ImageItem(Bitmap image, String path) {
        super();
        this.image = image;
        this.imagePath = path;
        this.tags = new ArrayList();
    }

    public void addTag(String tag){
        tags.add(tag);
    }

    public void removeTag(String tag){
        tags.remove(tag);
    }

    public ArrayList <String> getTags(){
        return tags;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPath() {
        return imagePath;
    }

    public void setPath(String path) {
        this.imagePath = path;
    }
}