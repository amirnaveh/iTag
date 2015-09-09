package com.amirnaveh.itag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by quickode037 on 8/13/15.
 */
public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public GridViewAdapter (Context context, int layoutResourceId, ArrayList data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        new AsyncTask<ViewHolder, String, Bitmap>() {
            private ViewHolder v;

            @Override
            protected Bitmap doInBackground(ViewHolder... params) {
                v = params[0];

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 20;
                Bitmap bitmap = BitmapFactory.decodeFile(v.path, options);

                return bitmap;

            }
        }.execute(holder);

        return row;
    }



//    private class GetImages extends AsyncTask<String, String, Bitmap > {
//
//        @Override
//        protected Bitmap doInBackground(String... path) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 20;
//            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//
//            return bitmap;
//        }

     class ViewHolder {
        String path;
        protected ImageView image;
    }
}
