package com.example.zloiy.customfileexplorer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZloiY on 25-Jul-16.
 */
public class FileArrayAdapter extends ArrayAdapter<Item> {

    private Context context;
    private int id;
    private List<Item> items;

    public FileArrayAdapter(Context context, int textViewresourceId, List<Item> objects){
        super(context, textViewresourceId, objects);
        this.context = context;
        id = textViewresourceId;
        items = objects;
    }

    public Item getItem(int i){
        return items.get(i);
    }

    public ArrayList<Item> getItems(){
        ArrayList<Item> list = new ArrayList<>();
        for (int i=0; i < items.size(); i++){
            list.add(getItem(i));
        }
        return list;
    }

    public View getView(int postion, View convertView, ViewGroup parent){
        View v = convertView;
        if (v==null){
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }
        final Item o = items.get(postion);
        if (o != null){
            TextView nameTV = (TextView) v.findViewById(R.id.name);
            TextView itemSizeTV = (TextView) v.findViewById(R.id.item_size);
            TextView dateTV = (TextView) v.findViewById(R.id.date);
            ImageView iconIV = (ImageView) v.findViewById(R.id.icon);
            String url = "drawable/" + o.getImage();
            int imageRes = context.getResources().getIdentifier(url, null, context.getPackageName());
            Drawable image = context.getResources().getDrawable(imageRes);
            iconIV.setImageDrawable(image);

            if (nameTV != null)
                nameTV.setText(o.getName());
            if (itemSizeTV != null)
                itemSizeTV.setText(o.getData());
            if (dateTV != null)
                dateTV.setText(o.getDate());
        }
        return  v;
    }
}
