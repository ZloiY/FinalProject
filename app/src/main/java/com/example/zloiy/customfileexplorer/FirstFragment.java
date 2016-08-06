package com.example.zloiy.customfileexplorer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FirstFragment extends AppCompatActivity {

    private File currentDir;
    private FileArrayAdapter adapter;
    private ListView listView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_fragment);
        ImageButton leftArrow = (ImageButton)findViewById(R.id.left_arrow);
        ImageButton homeBtn = (ImageButton)findViewById(R.id.home_btn);
        listView = (ListView) findViewById(R.id.listView);
        final FileSearch fileSearch = new FileSearch(FirstFragment.this, listView);
        currentDir = new File("/");
        final ArrayList<File> fastAccess = new ArrayList<>();
        final ArrayList<File> favorites = new ArrayList<>();
        fastAccess.add(new File(Environment.getRootDirectory().getPath()));
        fastAccess.add(new File(Environment.getExternalStorageDirectory().getPath()));
        fastAccess.add(new File(Environment.getDownloadCacheDirectory().getPath()));
        fileSearch.fillHome(fastAccess, favorites);
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstFragment.this, SecondFragment.class);
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
