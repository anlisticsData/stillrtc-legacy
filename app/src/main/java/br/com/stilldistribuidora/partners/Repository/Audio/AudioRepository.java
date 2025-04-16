package br.com.stilldistribuidora.partners.Repository.Audio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.partners.Models.Audio;
import br.com.stilldistribuidora.partners.Repository.EntitySqlite.AppOpeationEntitySqlite;
import br.com.stilldistribuidora.partners.Repository.EntitySqlite.AudioEntitySqlite;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.Repository.SqlCreateds.AppOpeationCreatedSql;

public class AudioRepository {
    private final Context context;
    private final AudioEntitySqlite audioEntitySqlite;

    public AudioRepository(Context context) {
        this.audioEntitySqlite = new AudioEntitySqlite(context);
        this.context = context;
    }

    public long createAudio(Audio audio){
        SQLiteDatabase db = this.audioEntitySqlite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put( AudioCreateTable.AudioTableColumns.TABLE_CODE_DELIVERY, audio.code);
        values.put( AudioCreateTable.AudioTableColumns.TABLE_CODE_PARTNER, audio.code_partner);
        values.put( AudioCreateTable.AudioTableColumns.TABLE_AUDIO_PATH, audio.audio);
        values.put( AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_TIME_DEVICE,audio.time_device);
        values.put( AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_LAT_LNG, audio.lat_lng);
        values.put( AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_CODE_ROUTER, audio.code_router);
        values.put( AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_UPLOAD,"N");
        long newRowId = db.insert(AudioCreateTable.AudioTableColumns.TABLE_NAME, null, values);
        return newRowId;
    }


    public List<Audio> notUploadFile(String codeDelivery,String  codePartner){
        List<Audio> amostras = new ArrayList<>();
        String filterUpload ="N";

        SQLiteDatabase db = this.audioEntitySqlite.getReadableDatabase();
        try {
            String[] projection = {
                    AudioCreateTable.AudioTableColumns.TABLE_ID,
                    AudioCreateTable.AudioTableColumns.TABLE_CODE_DELIVERY,
                    AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_LAT_LNG,
                    AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_TIME_DEVICE,
                    AudioCreateTable.AudioTableColumns.TABLE_AUDIO_PATH
            };
            String selection = AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_UPLOAD + " = ?"
                    +" and  "+AudioCreateTable.AudioTableColumns.TABLE_CODE_PARTNER+"=?  limit 0, 60 ";
            String[] selectionArgs = {filterUpload,codePartner};
            String sortOrder = null;
            Cursor cursor = db.query(
                    AudioCreateTable.AudioTableColumns.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
            while (cursor.moveToNext()) {
                Audio amostra = new Audio();
                amostra.id =((int)cursor.getLong(cursor.getColumnIndexOrThrow( AudioCreateTable.AudioTableColumns.TABLE_ID)));
                amostra.code_partner = codePartner;
                amostra.code =(cursor.getString(cursor.getColumnIndexOrThrow( AudioCreateTable.AudioTableColumns.TABLE_CODE_DELIVERY)));
                amostra.lat_lng =(cursor.getString(cursor.getColumnIndexOrThrow( AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_LAT_LNG)));
                amostra.time_device =(cursor.getString(cursor.getColumnIndexOrThrow( AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_TIME_DEVICE)));
                amostra.audio =(cursor.getString(cursor.getColumnIndexOrThrow( AudioCreateTable.AudioTableColumns.TABLE_AUDIO_PATH)));
                amostras.add(amostra);
            }
            cursor.close();
        } catch (Exception e) {
        }
        return amostras;

    }

    public boolean removeSample(int codeAudio,String pathFileSound){
        int resultDelete=0;
       try{
           File file = new File(pathFileSound);
           if(file.exists()){
               file.delete();
           }
           SQLiteDatabase db = this.audioEntitySqlite.getWritableDatabase();
           resultDelete = db.delete(AudioCreateTable.AudioTableColumns.TABLE_NAME, "id=?", new String[]{String.valueOf(codeAudio)});
           db.close();
       }catch (Exception e){
           e.printStackTrace();
       }
        return (resultDelete > 0 ) ? true : false;
    }




}
