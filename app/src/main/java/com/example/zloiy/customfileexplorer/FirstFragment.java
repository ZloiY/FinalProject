package com.example.zloiy.customfileexplorer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class FirstFragment extends AppCompatActivity {

    private File currentDir;
    private ListView listView;
    private FileSearch fileSearch;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_fragment);
        ImageButton leftArrow = (ImageButton)findViewById(R.id.left_arrow);
        ImageButton homeBtn = (ImageButton)findViewById(R.id.home_btn);
        listView = (ListView) findViewById(R.id.listView);
        fileSearch = new FileSearch(FirstFragment.this, listView);
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
                fileSearch.fillWithCheck(new File("/"));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setOptionsMenu(item);
        return true;
    }

    private void setOptionsMenu(MenuItem item){
        View layout;
        switch (item.getItemId()) {
            case R.id.new_folder:
                layout = getLayoutInflater().inflate(R.layout.new_folder, null);
                final EditText folderName = (EditText) layout.findViewById(R.id.folder_name);
                final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("Create folder")
                        .setView(layout)
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File curFolder = new File(fileSearch.getCurDir() + "/" + folderName.getText().toString());
                                if (!curFolder.exists()) curFolder.mkdir();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
                break;
            case R.id.search:
                layout = getLayoutInflater().inflate(R.layout.search_by_name, null);
                EditText searchByName = (EditText) layout.findViewById(R.id.file_name);
                searchByName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        fileSearch.fillByName(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
                        .setTitle("File search")
                        .setView(layout)
                        .setCancelable(true);
                builder1.create().show();
                break;
        }
    }
}
