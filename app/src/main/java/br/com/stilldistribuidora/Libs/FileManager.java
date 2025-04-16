package br.com.stilldistribuidora.Libs;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileManager {
    private final Context context;

    public FileManager(Context context) {
        this.context = context;
    }

    public String getRootPath() {
        File file = new File(context.getFilesDir(),"");
        return file.getAbsolutePath();
    }
    public String getFilePath(String name) {
        File file = new File(context.getFilesDir(), name);
        return file.getAbsolutePath();
    }

    public String sampleSounds() {
        return String.format("%s/amostras", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) );
    }
    public String cabinSonds() {
        return String.format("%s/cabin", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) );
    }

    public String createdDirectory(String dir) {
        return String.format("%s/%s",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),dir );
    }

    public  boolean createFileDirectory(File file) throws Exception {
        if (file.isDirectory()) {

        }else{
            try{
                // cria um objeto File pro diretorio pai
                File wallpaperDirectory = file;
                // cria a pasta caso seja necessario
                wallpaperDirectory.mkdirs();
                return true;
            }catch (Exception e){
                String message = e.getMessage();
                throw new Exception(message);
            }
        }
        return false;
    }



}
