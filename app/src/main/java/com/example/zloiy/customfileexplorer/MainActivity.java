package com.example.zloiy.customfileexplorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private File currentDir;
    private FileArrayAdapter adapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        currentDir = new File("/");
        fill(currentDir);
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
        adapter = new FileArrayAdapter(MainActivity.this, R.layout.list_layout, dir);
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
}
