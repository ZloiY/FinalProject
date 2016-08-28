package com.example.zloiy.customfileexplorer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;

public class FirstFragment extends Fragment {
    private ListView listView;
    private FileSearch fileSearch;
    private ActionMode actionMode;
    private FileOperations operations;
    private ToggleButton switchMode;
    private ViewGroup rootView;
    private String fileName;
    private String filePath;
    private String dir1;
    private boolean cut;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.setMenuVisibility(true);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.first_fragment, container, false);
        operations = new FileOperations(getContext());
        ImageView homeImage = (ImageView) rootView.findViewById(R.id.home_btn);
        switchMode = (ToggleButton)rootView.findViewById(R.id.switch_mode);
        listView = (ListView) rootView.findViewById(R.id.listView);
        registerForContextMenu(listView);
        fileSearch = new FileSearch(getActivity(), listView);
        final ArrayList<File> fastAccess = new ArrayList<>();
        final ArrayList<File> favorites = new ArrayList<>();
        fastAccess.add(Environment.getRootDirectory());
        fastAccess.add(Environment.getExternalStorageDirectory());
        fastAccess.add(new File(Environment.DIRECTORY_DOWNLOADS));
        fastAccess.add(new File(Environment.DIRECTORY_MUSIC));
        fastAccess.add(new File(Environment.DIRECTORY_DCIM));
        fastAccess.add(new File(Environment.DIRECTORY_PICTURES));
        if (dir1 != null) fileSearch.fill(new File(dir1));
        else fileSearch.fillHome(fastAccess,favorites);
        if (fileName != null) operations.setFile(fileName);
        if (filePath != null) operations.setInputPath(filePath);
        operations.cut = cut;
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    actionMode = getActivity().startActionMode(actionBar);
                    File file = new File(fileSearch.getCurDir());
                    fileSearch.fillWithCheck(file);
                }else{
                    actionMode.finish();
                    fileSearch.fill(new File(fileSearch.getCurDir()));
                }
            }
        });
        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSearch.fillHome(fastAccess, favorites);
            }
        });
        return rootView;
    }
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info){
        super.onCreateContextMenu(menu,view,info);
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
    }
    public String getFileName(){
        if(operations.getCurFile() != null){
            return operations.getCurFile();
        }else return null;
    }
    public String getFilePath(){
        if(operations.getInputPath() != null)
            return operations.getInputPath();
        else return null;
    }
    public boolean getCut(){
        return operations.cut;
    }
    public String getCurDir(){
        if (fileSearch.getCurDir() != null){
            return fileSearch.getCurDir();
        }else return null;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public void setFilePath(String filePath){
        this.filePath = filePath;
    }
    public void setCut(boolean cut){
        this.cut = cut;
    }
    public void setDir(String dir){
        dir1 =dir;
    }
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = menuInfo.position;
        final Item item1 =(Item)listView.getAdapter().getItem(index);
        ArrayList<Item> arrayList = new ArrayList<>();
        arrayList.add(item1);
        operations.setInputPathsNoCheck(arrayList, false);
        operations.setInputPath(fileSearch.getCurDir());
        switch (item.getItemId()){
            case R.id.copy:
                operations.setCut(false);
                Toast.makeText(getContext(),"File copy",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cut:
                operations.setCut(true);
                Toast.makeText(getContext(),"File cut", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                operations.deleteFile(item1.getName(),fileSearch.getCurDir());
                fileSearch.fill(new File(fileSearch.getCurDir()));
                break;
            case R.id.change_name:
                View layout = getActivity().getLayoutInflater().inflate(R.layout.new_folder,null);
                final EditText changeName = (EditText)layout.findViewById(R.id.folder_name);
                changeName.setText(item1.getName());
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setView(layout)
                        .setTitle("Change name")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(item1.getPath());
                                boolean rn = file.renameTo(new File(fileSearch.getCurDir() +"/"+ changeName.getText().toString()));
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
                builder.create().show();
                break;
        }
        return true;
    }
    private ActionMode.Callback actionBar = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_bar, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.delete:
                    MultiplyChekAdapter adapter = (MultiplyChekAdapter) listView.getAdapter();
                    operations.setInputPaths(adapter.getArrayList(), false);
                    operations.multiDelFiles();
                    fileSearch.fillWithCheck(new File(fileSearch.getCurDir()));
                    break;
                case R.id.copy:
                    MultiplyChekAdapter copyAdapter = (MultiplyChekAdapter) listView.getAdapter();
                    operations.setInputPath(fileSearch.getCurDir());
                    operations.setInputPaths(copyAdapter.getArrayList(), false);
                    Toast.makeText(getContext(),operations.getInputPaths().size() + " files copy", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.cut:
                    MultiplyChekAdapter copyAdapter1 = (MultiplyChekAdapter) listView.getAdapter();
                    operations.setInputPath(fileSearch.getCurDir());
                    operations.setInputPaths(copyAdapter1.getArrayList(), true);
                    Toast.makeText(getContext(),operations.getInputPaths().size() + " files cut", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        setOptionsMenu(item);
        return true;
    }
    private void setOptionsMenu(MenuItem item){
        View layout;
        switch (item.getItemId()) {
            case R.id.new_folder:
                layout = getActivity().getLayoutInflater().inflate(R.layout.new_folder, null);
                final EditText folderName = (EditText) layout.findViewById(R.id.folder_name);
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext())
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
                layout = getActivity().getLayoutInflater().inflate(R.layout.search_by_name, null);
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
                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setTitle("File search")
                        .setView(layout)
                        .setCancelable(true);
                builder1.create().show();
                break;
            case R.id.paste:
                if(!operations.getInputPath().isEmpty()) operations.copyInAnotherTask(fileSearch.getCurDir());
                Toast.makeText(getContext(), "Files paste", Toast.LENGTH_SHORT).show();
                fileSearch.fill(new File(fileSearch.getCurDir()));
                break;
            case R.id.new_file:
                layout = getActivity().getLayoutInflater().inflate(R.layout.new_folder, null);
                final EditText fileName= (EditText)layout.findViewById(R.id.folder_name);
                android.support.v7.app.AlertDialog.Builder builder2 = new android.support.v7.app.AlertDialog.Builder(getContext())
                        .setTitle("New file")
                        .setCancelable(true)
                        .setView(layout)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                operations.newFile(fileName.getText().toString(),fileSearch.getCurDir(), getContext());
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
