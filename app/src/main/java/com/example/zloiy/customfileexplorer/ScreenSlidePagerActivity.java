package com.example.zloiy.customfileexplorer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ZloiY on 27-Aug-16.
 */
public class ScreenSlidePagerActivity extends FragmentActivity {
    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FileSearch fileSearch;
    private FileOperations operations;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setPageTransformer(true, new DepthPageTransformer());
        //fileSearch = new FileSearch()
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }
    public void onBackPressed() {
        super.onBackPressed();
        if(mPager.getCurrentItem() == 0){
            super.onBackPressed();
        }else{
            mPager.setCurrentItem(mPager.getCurrentItem() -1);
        }
    }
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{
        public ScreenSlidePagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new FirstFragment();
                case 1: return new SecondFragment();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

   protected void onSaveInstanceState(Bundle outState) {
       super.onSaveInstanceState(outState);
       if (operations.getInputPath() != null)
           outState.putString("copyFilePath", operations.getInputPath());
       if (operations.getCurFile()!= null)
           outState.putString("copyFileName", operations.getCurFile());
       outState.putBoolean("cut", operations.cut);
       if (fileSearch.getCurDir() != null)
           outState.putString("curDir1", fileSearch.getCurDir());
      /* if (curDir2 != null)
           outState.putString("curDir2",curDir2);*/
   }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
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
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Create folder")
                        .setView(layout)
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File curFolder = new File(fileSearch.getCurDir() + "/" + folderName.getText().toString());
                                if (!curFolder.exists()) curFolder.mkdir();
                                fileSearch.fill(new File(fileSearch.getCurDir()));
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
                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("File search")
                        .setView(layout)
                        .setCancelable(true);
                builder1.create().show();
                break;
            case R.id.paste:
                if(!operations.getInputPath().isEmpty()) operations.copyInAnotherTask(fileSearch.getCurDir());
                Toast.makeText(this, "Files paste", Toast.LENGTH_SHORT).show();
                fileSearch.fill(new File(fileSearch.getCurDir()));
                break;
            case R.id.new_file:
                layout = getLayoutInflater().inflate(R.layout.new_folder, null);
                final EditText fileName= (EditText)layout.findViewById(R.id.folder_name);
                android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("New file")
                        .setCancelable(true)
                        .setView(layout)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                operations.newFile(fileName.getText().toString(),fileSearch.getCurDir(), getBaseContext());
                                fileSearch.fill(new File(fileSearch.getCurDir()));
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder2.create().show();
                break;
        }
    }
}
