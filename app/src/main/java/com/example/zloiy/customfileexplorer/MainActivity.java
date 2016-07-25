package com.example.zloiy.customfileexplorer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView firstList = (ListView) findViewById(R.id.firstlist);
        path = "/";
        if (getIntent().hasExtra("path")){
            path = getIntent().getStringExtra("path");
        }
        setTitle(path);
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()){
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null){
            for (String file:list){
                if (!file.startsWith("."))
                    values.add(file);
            }
        }
        Collections.sort(values);
        ArrayAdapter adapter = new ArrayAdapter<ArrayList<String>>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, values);
        firstList.setAdapter(adapter);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = (String) parent.getAdapter().getItem(position);
                if (path.endsWith(File.separator))
                    filename = path + filename;
                else filename = path + File.separator + filename;
                if (new File(filename).isDirectory()){
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    intent.putExtra("path", filename);
                    startActivity(intent);
                }else
                    Toast.makeText(MainActivity.this, filename + "is not a directory", Toast.LENGTH_SHORT).show();
            }
        };
        firstList.setOnItemClickListener(itemClickListener);
    }
}
