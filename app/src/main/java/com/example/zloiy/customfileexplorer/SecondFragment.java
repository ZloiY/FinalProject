package com.example.zloiy.customfileexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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
public class SecondFragment extends AppCompatActivity {
    private ListView listView;
    private FileArrayAdapter adapter;
    private File currentDir;
    protected void onCreate(Bundle onSavedInstances){
        super.onCreate(onSavedInstances);
        setContentView(R.layout.second_fargment);
        listView = (ListView) findViewById(R.id.fast_access);
        ImageButton rightArrow = (ImageButton)findViewById(R.id.right_arrow);
        ImageButton homeBtn = (ImageButton)findViewById(R.id.home_btn);
        final ArrayList<File> fastAccess = new ArrayList<>();
        final ArrayList<File> favorites = new ArrayList<>();
        final FileSearch fileSearch = new FileSearch(SecondFragment.this,listView);
        fastAccess.add(new File(Environment.getRootDirectory().getPath()));
        fastAccess.add(new File(Environment.getExternalStorageDirectory().getPath()));
        fastAccess.add(new File(Environment.getDownloadCacheDirectory().getPath()));
        fileSearch.fillHome(fastAccess, favorites);
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
                fileSearch.fillHome(fastAccess,favorites);
            }
        });
    }
}
