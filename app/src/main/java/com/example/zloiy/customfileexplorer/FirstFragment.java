package com.example.zloiy.customfileexplorer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;

public class FirstFragment extends AppCompatActivity {
    private ListView listView;
    private FileSearch fileSearch;
    private ActionMode actionMode;
    private FileOperations operations;
    private String curDir2;
    private ToggleButton switchMode;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_fragment);
        operations = new FileOperations(FirstFragment.this);
        ImageView leftArrow = (ImageView) findViewById(R.id.left_arrow);
        ImageView homeImage = (ImageView) findViewById(R.id.home_btn);
        switchMode = (ToggleButton)findViewById(R.id.switch_mode);
        listView = (ListView) findViewById(R.id.listView);
        registerForContextMenu(listView);
        fileSearch = new FileSearch(FirstFragment.this, listView);
        final ArrayList<File> fastAccess = new ArrayList<>();
        final ArrayList<File> favorites = new ArrayList<>();
        fastAccess.add(new File(Environment.getRootDirectory().getPath()));
        fastAccess.add(new File(Environment.getExternalStorageDirectory().getPath()));
        fastAccess.add(new File(Environment.DIRECTORY_DOWNLOADS));
        fastAccess.add(new File(Environment.DIRECTORY_MUSIC));
        fastAccess.add(new File(Environment.DIRECTORY_DCIM));
        fastAccess.add(new File(Environment.DIRECTORY_PICTURES));
        if (savedInstanceState != null){
            if (savedInstanceState.getString("copyFilePath") != null && savedInstanceState.getString("copyFileName") != null) {
                ArrayList<Item> list = new ArrayList<>();
                list.add(new Item(savedInstanceState.getString("copyFileName"),"","",savedInstanceState.getString("copyFilePath")+"/"+savedInstanceState.getString("copyFileName"),"", true));
                operations.setInputPath(savedInstanceState.getString("copyFilePath"));
                operations.setInputPaths(list, savedInstanceState.getBoolean("cut"));
            }
            if (savedInstanceState.getString("curDir2") != null)
                curDir2 = savedInstanceState.getString("curDir2");
            if (savedInstanceState.getString("curDir1") != null)
                fileSearch.fill(new File(savedInstanceState.getString("curDir1")));
            else fileSearch.fillHome(fastAccess,favorites);
        }else {
            Intent intent = getIntent();
            if (intent.hasExtra("curDirectory1")) {
                String dirPath = intent.getStringExtra("curDirectory1");
                if (!dirPath.isEmpty())
                    fileSearch.fill(new File(dirPath));
            } else {
                fileSearch.fillHome(fastAccess, favorites);
            }
            if (intent.hasExtra("curDirectory2")) {
                curDir2 = intent.getStringExtra("curDirectory2");
            }
            if (intent.hasExtra("copyFilePath") && intent.hasExtra("copyFileName")) {
                String copyFilePath = intent.getStringExtra("copyFilePath");
                String copyFileName = intent.getStringExtra("copyFileName");
                boolean cut = intent.getBooleanExtra("cut", false);
                if (copyFileName != null && copyFilePath != null) {
                    ArrayList<Item> list = new ArrayList<>();
                    list.add(new Item(copyFileName, "", "", copyFilePath + "/" + copyFileName, "", true));
                    operations.setInputPath(copyFilePath);
                    operations.setInputPaths(list, cut);
                    operations.setFile(copyFileName);
                }
            }
        }
        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    actionMode = startActionMode(actionBar);
                    fileSearch.fillWithCheck(new File(fileSearch.getCurDir()));
                }else{
                    actionMode.finish();
                    fileSearch.fill(new File(fileSearch.getCurDir()));
                }
            }
        });
        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstFragment.this, SecondFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(fileSearch.getCurDir() != null) {
                    intent.putExtra("curDirectory1", fileSearch.getCurDir());
                }
                intent.putExtra("copyFilePath", operations.getInputPath());
                intent.putExtra("copyFileName", operations.getCurFile());
                intent.putExtra("cut", operations.cut);
                if (curDir2 != null) {
                    intent.putExtra("curDirectory2", curDir2);
                }
                startActivity(intent);
            }
        });
        homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileSearch.fillHome(fastAccess, favorites);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(this)
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
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this)
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
                AlertDialog.Builder builder2 = new AlertDialog.Builder(this)
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
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info){
        super.onCreateContextMenu(menu,view,info);
        getMenuInflater().inflate(R.menu.context_menu, menu);
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
                Toast.makeText(this,"File copy",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cut:
                operations.setCut(true);
                Toast.makeText(this,"File cut", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                operations.deleteFile(item1.getName(),fileSearch.getCurDir());
                fileSearch.fill(new File(fileSearch.getCurDir()));
                break;
            case R.id.change_name:
                View layout = getLayoutInflater().inflate(R.layout.new_folder,null);
                final EditText changeName = (EditText)layout.findViewById(R.id.folder_name);
                changeName.setText(item1.getName());
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
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
                    Toast.makeText(getBaseContext(),operations.getInputPaths().size() + " files copy", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.cut:
                    MultiplyChekAdapter copyAdapter1 = (MultiplyChekAdapter) listView.getAdapter();
                    operations.setInputPath(fileSearch.getCurDir());
                    operations.setInputPaths(copyAdapter1.getArrayList(), true);
                    Toast.makeText(getBaseContext(),operations.getInputPaths().size() + " files cut", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //actionMode = null;
        }
    };
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (operations.getInputPath() != null)
            outState.putString("copyFilePath", operations.getInputPath());
        if (operations.getCurFile()!= null)
            outState.putString("copyFileName", operations.getCurFile());
        outState.putBoolean("cut", operations.cut);
        if (fileSearch.getCurDir() != null)
            outState.putString("curDir1", fileSearch.getCurDir());
        if (curDir2 != null)
            outState.putString("curDir2",curDir2);
    }
}
