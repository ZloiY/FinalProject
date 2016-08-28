package com.example.zloiy.customfileexplorer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

/**
 * Created by ZloiY on 27-Aug-16.
 */
public class ScreenSlidePagerActivity extends FragmentActivity {
    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private FirstFragment fragment1;
    private SecondFragment fragment2;
    private String copyPath;
    private String copyFile;
    private boolean cut;
    private String dir1;
    private String dir2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        if (savedInstanceState != null){
            if(savedInstanceState.getString("dir1")!=null)
                dir1=savedInstanceState.getString("dir1");
            if(savedInstanceState.getString("dir2")!=null)
                dir2=savedInstanceState.getString("dir2");
            if(savedInstanceState.getBoolean("cut", false))
                cut = savedInstanceState.getBoolean("cut");
            if(savedInstanceState.getString("copyPath")!=null)
                copyPath=savedInstanceState.getString("copyPath");
            if(savedInstanceState.getString("copyFile")!=null)
                copyFile=savedInstanceState.getString("copyFile");
        }
        mPager = (ViewPager) findViewById(R.id.view_pager);
        mPager.setPageTransformer(true, new DepthPageTransformer());
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
                case 0: fragment1 = new FirstFragment();
                    fragment1.setHasOptionsMenu(true);
                    fragment1.setCut(cut);
                    fragment1.setFileName(copyFile);
                    fragment1.setFilePath(copyPath);
                    fragment1.setDir(dir1);
                    return fragment1;
                case 1:
                    fragment2 = new SecondFragment();
                    fragment2.setCut(cut);
                    fragment2.setFileName(copyFile);
                    fragment2.setFilePath(copyPath);
                    fragment2.setDir(dir2);
                    return fragment2;
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
       if (getCurrentFocus() == fragment1.getView()){
           outState.putString("dir1", fragment1.getCurDir());
           outState.putString("dir2", fragment2.getCurDir());
           outState.putString("copyFile", fragment1.getFileName());
           outState.putString("copyPath", fragment1.getFilePath());
           outState.putBoolean("cut", fragment1.getCut());
       }else{
           outState.putString("dir1", fragment1.getCurDir());
           outState.putString("dir2", fragment2.getCurDir());
           outState.putString("copyFile", fragment2.getFileName());
           outState.putString("copyPath", fragment2.getFilePath());
           outState.putBoolean("cut", fragment2.getCut());
       }
   }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    /*public boolean onOptionsItemSelected(MenuItem item) {
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
    }*/
}
