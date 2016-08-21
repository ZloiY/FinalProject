package com.example.zloiy.customfileexplorer;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZloiY on 20-Aug-16.
 */
public class FileOperations {
    private String inputPath;
    private String file;
    private ArrayList<String> inputPaths;
    private Context context;
    private boolean multi;
    boolean cut;
    FileOperations(Context context){
        inputPath="";
        multi = false;
        cut = false;
        inputPaths = new ArrayList<>();
        this.context = context;
    }
    FileOperations(){
        inputPath="";
        multi = false;
        cut = false;
        inputPaths = new ArrayList<>();
    }
    public void setInputPath(String inputPath, String file, boolean cut){
        multi =false;
        this.file = file;
        this.inputPath = inputPath;
        this.cut = cut;
    }
    public void setInputPath(String inputPath){
        this.inputPath = inputPath;
    }
    public String getInputPath(){return inputPath;}
    public ArrayList<String> getInputPaths(){return  inputPaths;}
    public void setInputPaths(ArrayList<Item> inputArray, boolean cut){
        this.cut = cut;
        this.multi = true;
        for (int index = 0; index<inputArray.size(); index++){
            if (inputArray.get(index).isCheck())
                inputPaths.add(inputArray.get(index).getPath());
        }
    }
    public void copyFile(String outputPath){
        InputStream in = null;
        OutputStream out = null;
        try {
            if (!multi) {
                File dir = new File(outputPath);
                in = new FileInputStream(inputPath + "/" + file);
                out = new FileOutputStream(outputPath + "/" + file);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                if (cut) new File(inputPath + "/" + file).delete();
            }else{
                CopyTask copyTask = new CopyTask(context, outputPath, inputPath, cut);
                List<File> files = new ArrayList<>();
                for (int curFile=0; curFile<inputPaths.size(); curFile++){
                    files.add(new File(inputPaths.get(curFile)));
                }
                File[] filesArray = files.toArray(new File[files.size()]);
                copyTask.execute(filesArray);
            }

        }catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
    public void deleteFile(String file, String inputPath){
        try{
            File deleteFile = new File(inputPath +"/"+ file);
            boolean deleted = deleteFile.delete();
        }catch (Exception e){
            Log.e("tag", e.getMessage());
        }
    }
    public void newFile(String fileName, String inputPath, Context context){
        try{
            File file = new File(inputPath + "/" + fileName);
            if (!file.exists()) {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.flush();
                fileWriter.close();
            }
        }catch (Exception e){
            Log.e("tag", e.getMessage());
        }
    }
    public void multiDelFiles(){
        try{
            for (int i=0; i < inputPaths.size(); i++){
                File delFile = new File(inputPaths.get(i));
                boolean deleted = delFile.delete();
            }
        }catch (Exception e){
            Log.e("tag", e.getMessage());
        }
    }
}
