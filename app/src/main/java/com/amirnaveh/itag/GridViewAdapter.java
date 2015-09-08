package com.amirnaveh.itag;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
//            holder.imagePath = (TextView) row.findViewById(R.id.text);
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        }
        else {
            holder = (ViewHolder) row.getTag();
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 20;


        Bitmap bitmap = BitmapFactory.decodeFile((String)data.get(position), options);

//        Bitmap compressed = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
//        try{
//            long time = System.currentTimeMillis();
//            File dumpFile = new File(time + ".png");
//            os = new FileOutputStream(dumpFile);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 150, os);
//        }
//        catch (IOException ioe) {
//            Log.e("crap", "GrayImage dump failed", ioe);
//        }
        ImageItem item = new ImageItem(bitmap, (String)data.get(position));
//        holder.imagePath.setText(item.getPath());
        holder.image.setImageBitmap(item.getImage());
//        bitmap.recycle();
        return row;
    }

    static class ViewHolder {
        TextView imagePath;
        ImageView image;
    }
}
