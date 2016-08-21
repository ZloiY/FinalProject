package com.example.zloiy.customfileexplorer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.File;

/**
 * Created by ZloiY on 21-Aug-16.
 */
public class CopyTask extends AsyncTask<File, Long, Boolean> {
    ProgressDialog progress;
    Context context;
    String outputPath;
    String inputPath;
    boolean cut;
    FileOperations operations;
    public CopyTask(Context context, String outputPath, String inputPath, boolean cut){
        this.context=context;
        this.outputPath=outputPath;
        this.inputPath = inputPath;
        this.cut = cut;
        operations = new FileOperations();
    }
    protected void onPreExecute() {
        progress = ProgressDialog.show(context,"","Loading...",true);
    }
    protected Boolean doInBackground(File... files) {
        for (int cutFile=0; cutFile<files.length; cutFile++){
            operations.setInputPath(inputPath, files[cutFile].getName(), cut);
            operations.copyFile(outputPath);
        }
        return true;
    }
    protected void onPostExecute(Boolean success) {
        progress.dismiss();
        // Show dialog with result
    }
    protected void onProgressUpdate(Long... values) {
        for (int numFile=0; numFile<values.length; numFile++) {
            progress.setMessage("Transferred " + values[numFile] + "files");
        }
    }
}
