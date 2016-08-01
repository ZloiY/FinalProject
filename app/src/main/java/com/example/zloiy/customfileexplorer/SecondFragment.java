package com.example.zloiy.customfileexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by ZloiY on 28-Jul-16.
 */
public class SecondFragment extends Activity {
    private ListView listView;
    private FileArrayAdapter adapter;
    private File currentDir;
    protected void onCreate(Bundle onSavedInstances){
        super.onCreate(onSavedInstances);
        setContentView(R.layout.second_fargment);
        listView = (ListView) findViewById(R.id.fast_access);
        CheckBox checkAll = (CheckBox) findViewById(R.id.chk_all);
        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                int itemCount = listView.getCount();
                for (int i =0; i<itemCount; i++)
                    listView.setItemChecked(i, checkBox.isChecked());
            }
        });
        ImageButton rightArrow = (ImageButton)findViewById(R.id.right_arrow);
        ImageButton homeBtn = (ImageButton)findViewById(R.id.home_btn);
        ImageButton copyBtn = (ImageButton)findViewById(R.id.cpy_btn);
        final ArrayList<File> fastAccess = new ArrayList<>();
        final ArrayList<File> favorites = new ArrayList<>();
        fastAccess.add(new File("/"));
        fastAccess.add(new File("/sdcard/"));
        fastAccess.add(new File("/sdcard/Download"));
        fillHome(fastAccess, favorites);
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondFragment.this, FirstFragment.class);
                startActivity(intent);
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillHome(fastAccess,favorites);
            }
        });
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckList();
            }
        });
    }
    private void fillHome (final ArrayList fastAccess, final ArrayList favorites){
        SecondFragment.this.setTitle("Select category: ");
        File currentFile;
        List<Item>dir = new ArrayList<>();
        try{
            for (int curFile=0; curFile < fastAccess.size(); curFile++){
                currentFile = new File(fastAccess.get(curFile).toString());
                Date lastModifyDate = new Date(currentFile.lastModified());
                DateFormat format = DateFormat.getDateInstance();
                String date_modify = format.format(lastModifyDate);
                File[] fbuf = currentFile.listFiles();
                int buf = 0;
                if (fbuf != null){
                    buf = fbuf.length;
                }else buf =0;
                    String num_items = String.valueOf(buf);
                if (buf == 0) num_items = num_items + "item";
                else num_items = num_items + " items";
                if (curFile == 0)
                    dir.add(new Item(currentFile.getName(), num_items, date_modify, currentFile.getAbsolutePath(),"root_folder"));
                if (curFile==1)
                    dir.add(new Item(currentFile.getName(), num_items, date_modify, currentFile.getAbsolutePath(),"sd_card_icon"));
                if (curFile==2)
                    dir.add(new Item(currentFile.getName(), num_items, date_modify, currentFile.getAbsolutePath(),"download_folder"));
            }
        }
        catch (Exception e){
            e.getStackTrace();
        }
        Collections.sort(dir);
        adapter = new FileArrayAdapter(SecondFragment.this, R.layout.list_layout, dir);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = adapter.getItem(position);
                     currentDir = new File(item.getPath());
                    fill(currentDir);
            }
        });
    }
    private void fill (final File file){
        File[] dirs = file.listFiles();
        this.setTitle("Current Dir: "+file.getName());
        List<Item>dir = new ArrayList<>();
        List<Item>fls = new ArrayList<>();
        try{
            for (File ff: dirs){
                Date lastModifyDate = new Date(ff.lastModified());
                DateFormat format = DateFormat.getDateInstance();
                String date_modify = format.format(lastModifyDate);
                if (ff.isDirectory()){
                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if (fbuf != null){
                        buf = fbuf.length;
                    }else buf =0;
                    String num_items = String.valueOf(buf);
                    if (buf == 0) num_items = num_items + "item";
                    else num_items = num_items + " items";
                    dir.add(new Item(ff.getName(), num_items, date_modify, ff.getAbsolutePath(),"folder_icon"));}
                else{
                    fls.add(new Item(ff.getName(), ff.length() + "Bytes", date_modify, ff.getAbsolutePath(), "file_icon"));
                }
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!file.getName().equalsIgnoreCase("/"))
            dir.add(0, new Item("..", "ParentDirectory", "", file.getParent(), "upfolder_icon"));
        adapter = new FileArrayAdapter(SecondFragment.this, R.layout.list_layout, dir);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = adapter.getItem(position);
                if (item.getImage().equalsIgnoreCase("folder_icon")){
                    currentDir = new File(item.getPath());
                    fill(currentDir);
                }else
                if (item.getImage().equalsIgnoreCase("upfolder_icon")){
                    if (currentDir.getParent() == null);
                    else {
                        currentDir = new File(item.getPath());
                        fill(currentDir);
                    }
                }else
                    onFileClick(item);
            }
        });
    }
    private void onFileClick(Item item){
        Intent intent = new Intent();
        intent.putExtra("GetPath", currentDir.toString());
        intent.putExtra("GetFileName", item.getName());
        setResult(RESULT_OK, intent);
        finish();
    }
    private void setCheckList(){
        List<Item> allFiles = adapter.getItems();
        ArrayAdapter<Item> checkAdapter = new ArrayAdapter<Item>(SecondFragment.this, android.R.layout.simple_list_item_multiple_choice, allFiles);
        listView.setAdapter(checkAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) findViewById(R.id.chk_all);
                int checkedItemCount = getCheckedItemCount();
                if(listView.getCount() == checkedItemCount) checkBox.setChecked(true);
                else checkBox.setChecked(false);
            }
        });
    }
    private int getCheckedItemCount(){
        int cnt = 0;
        SparseBooleanArray positions = listView.getCheckedItemPositions();
        int itemCount = listView.getCount();

        for (int i = 0; i < itemCount; i++) {
            if (positions.get(i)) cnt++;
        }

        return cnt;
    }
}
