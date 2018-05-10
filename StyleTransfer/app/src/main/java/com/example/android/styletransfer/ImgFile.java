package com.example.android.styletransfer;

import java.io.File;

/**
 * Created by lcy on 2018/4/30.
 */

public class ImgFile {
    private static File imgFile;
    private static File croppedFile;

    ImgFile(File f){
        imgFile = f;
    }

    ImgFile(){}

    public void setFile(File f){
        imgFile = f;
    }

    public void setCroppedFile(File f){
        croppedFile = f;
    }

    public File getFile(){
        return imgFile;
    }

    public File getCroppedFile(){
        return croppedFile;
    }
}
