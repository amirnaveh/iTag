package com.amirnaveh.itag;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mattan on 04/09/2015.
 */
public class TagGridViewAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public TagGridViewAdapter(Context context, int layoutResourceId, ArrayList data){
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViewHolder holder;
        if( null == row){
            LayoutInflater inflater = ((Activity)context ).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.txtViewTag = (TextView) row.findViewById(R.id.text_tag_item);
            row.setTag(holder);
        }
        else{
            holder = (ViewHolder) row.getTag();
        }

        String tag = (String)data.get(position);
        holder.txtViewTag.setText(tag);

        return row;
    }

    public ArrayList getData() {
        return this.data;
    }

    static class ViewHolder{
        TextView txtViewTag;
    }
}
