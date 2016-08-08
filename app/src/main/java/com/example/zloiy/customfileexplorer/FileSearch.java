package com.example.zloiy.customfileexplorer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by ZloiY on 06-Aug-16.
 */
public class FileSearch {
    private AppCompatActivity curActivity;
    private ListView listView;
    private File curDir;
    private FileArrayAdapter adapter;
    public FileSearch(SecondFragment activity, ListView listView){
        curActivity = activity;
        this.listView = listView;
    }
    public FileSearch(FirstFragment activity, ListView listView){
        curActivity = activity;
        this.listView = listView;
    }

    public void fillHome(ArrayList fastAccess, ArrayList favorites){
        curActivity.setTitle("Select category: ");
        File currentFile;
        List<Item> dir = new ArrayList<>();
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
        adapter = new FileArrayAdapter(curActivity, R.layout.list_layout, dir);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = adapter.getItem(position);
                curDir = new File(item.getPath());
                fill(curDir);
            }
        });
    }
    private void fill (final File file){
        File[] dirs = file.listFiles();
        curActivity.setTitle("Current Dir: "+file.getName());
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
        adapter = new FileArrayAdapter(curActivity, R.layout.list_layout, dir);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = adapter.getItem(position);
                if (item.getImage().equalsIgnoreCase("folder_icon")){
                    curDir = new File(item.getPath());
                    fill(curDir);
                }else
                if (item.getImage().equalsIgnoreCase("upfolder_icon")){
                    if (curDir.getParent() == null);
                    else {
                        curDir = new File(item.getPath());
                        fill(curDir);
                    }
                }else
                    onFileClick(item);
            }
        });
    }
    public void fillWithCheck (final File file){
        File[] dirs = file.listFiles();
        MultiplyChekAdapter adapter;
        curActivity.setTitle("Current Dir: "+file.getName());
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
                    dir.add(new Item(ff.getName(), num_items, date_modify, ff.getAbsolutePath(),"folder_icon", false));}
                else{
                    fls.add(new Item(ff.getName(), ff.length() + "Bytes", date_modify, ff.getAbsolutePath(), "file_icon", false));
                }
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
       /* if (!file.getName().equalsIgnoreCase("/"))
            dir.add(0, new Item("..", "ParentDirectory", "", file.getParent(), "upfolder_icon"));*/
        adapter = new MultiplyChekAdapter(curActivity, R.layout.check_list, dir);
        listView.setAdapter(adapter);
    }
    public void fillByName (String name){
        File[] dirs = curDir.listFiles();
        curActivity.setTitle("Current Dir: "+curDir.getName());
        List<Item>dir = new ArrayList<>();
        List<Item>fls = new ArrayList<>();
        try{
            for (File ff: dirs){
                Date lastModifyDate = new Date(ff.lastModified());
                DateFormat format = DateFormat.getDateInstance();
                String date_modify = format.format(lastModifyDate);
                if (ff.isDirectory() && ff.getName().contains(name)){
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
                    if (ff.getName().contains(name))
                    fls.add(new Item(ff.getName(), ff.length() + "Bytes", date_modify, ff.getAbsolutePath(), "file_icon"));
                }
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if (!curDir.getName().equalsIgnoreCase("/"))
            dir.add(0, new Item("..", "ParentDirectory", "", curDir.getParent(), "upfolder_icon"));
        adapter = new FileArrayAdapter(curActivity, R.layout.list_layout, dir);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = adapter.getItem(position);
                if (item.getImage().equalsIgnoreCase("folder_icon")){
                    curDir = new File(item.getPath());
                    fill(curDir);
                }else
                if (item.getImage().equalsIgnoreCase("upfolder_icon")){
                    if (curDir.getParent() == null);
                    else {
                        curDir = new File(item.getPath());
                        fill(curDir);
                    }
                }else
                    onFileClick(item);
            }
        });
    }
    private void onFileClick(Item item){
        Intent intent = new Intent();
        intent.putExtra("GetPath", curDir.toString());
        intent.putExtra("GetFileName", item.getName());
        curActivity.setResult(curActivity.RESULT_OK, intent);
        curActivity.recreate();
    }
    public String getCurDir(){return curDir.getPath();}
}
