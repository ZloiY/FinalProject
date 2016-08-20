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

/**
 * Created by ZloiY on 20-Aug-16.
 */
public class FileOperations {
    private String inputPath;
    private String file;
    private ArrayList<String> inputPaths;
    boolean cut;
    FileOperations(){
        inputPath="";
        inputPaths = new ArrayList<>();
    }
    public void setInputPath(String inputPath, String file, boolean cut){
        this.file = file;
        this.inputPath = inputPath;
        this.cut = cut;
    }
    public String getInputPath(){return inputPath;}
    public void setInputPaths(String[] inputArray, boolean cut){
        this.cut = cut;
        for(int i=0; i < inputArray.length; i++){
             inputPaths.add(inputArray[i]);
        }
    }
    public void copyFile(String outputPath){
        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File(outputPath);
            in = new FileInputStream(inputPath +"/"+file);
            out = new FileOutputStream(outputPath +"/"+file);
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
            if(cut) new File(inputPath +"/"+file).delete();

        }catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
    public  void deleteFile(String file, String inputPath){
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
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e){
            Log.e("tag", e.getMessage());
        }
    }
}
