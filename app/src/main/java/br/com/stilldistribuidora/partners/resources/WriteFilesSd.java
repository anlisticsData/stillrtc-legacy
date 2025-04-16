package br.com.stilldistribuidora.partners.resources;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import br.com.stilldistribuidora.Libs.Libs;
import br.com.stilldistribuidora.partners.Contracts.OnResponseFile;

public class WriteFilesSd {
    private OnResponseFile onResponseFile;
    public WriteFilesSd(OnResponseFile onResponseFile){
        this.onResponseFile=onResponseFile;
    }


    public void generateNoteOnSD(String sFileName, String sBody) {
        try {

            String dateTime = CommonFunctions.getDate();
            String sBodyS=String.format("%s , %s ",dateTime,sBody);
            File root = new File(Environment.getExternalStorageDirectory(), "Logs");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile,true);
            writer.append(sBodyS);
            writer.write("\n");
            writer.flush();
            writer.close();
            onResponseFile.onResult(true,"ok");
        } catch (IOException e) {
            e.printStackTrace();
            onResponseFile.onResult(false,e.getMessage());
        }
    }

}
