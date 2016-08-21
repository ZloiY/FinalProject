package com.example.zloiy.customfileexplorer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZloiY on 08-Aug-16.
 */
public class MultiplyChekAdapter extends ArrayAdapter<Item> {

    private Context context;
    private List<Item> itemCheckList;
    private LayoutInflater inflater;
    private int id;

    public MultiplyChekAdapter(Context context, int layoutId, List<Item> objects){
        super(context, layoutId, objects);
        this.context = context;
        id = layoutId;
        itemCheckList = objects;
    }

    @Override
    public int getCount() {
        return itemCheckList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(id, null);
        }
        final Item item = itemCheckList.get(position);
        if (item != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.icon1);
            TextView nameTV = (TextView) view.findViewById(R.id.name);
            TextView sizeTV = (TextView) view.findViewById(R.id.item_size);
            TextView dateTV = (TextView) view.findViewById(R.id.date);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.chk_box);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setCheck();
                }
            });
            String url = "drawable/" + item.getImage();
            int imageRes = context.getResources().getIdentifier(url, null, context.getPackageName());
            Drawable image = context.getResources().getDrawable(imageRes);
            imageView.setImageDrawable(image);
            if (nameTV != null) nameTV.setText(item.getName());
            if (sizeTV != null) sizeTV.setText(item.getData());
            if (dateTV != null) dateTV.setText(item.getDate());
            checkBox.setChecked(item.isCheck());
        }
        return view;
    }

    public ArrayList<Item> getArrayList(){
        ArrayList<Item> list = new ArrayList<>();
        for (int i=0; i < itemCheckList.size(); i++)
            list.add(itemCheckList.get(i));
        return list;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Item getItem(int position) {
        return itemCheckList.get(position);
    }
}
