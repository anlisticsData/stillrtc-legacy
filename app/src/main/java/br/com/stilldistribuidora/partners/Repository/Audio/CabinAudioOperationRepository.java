package br.com.stilldistribuidora.partners.Repository.Audio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.partners.Models.Audio;
import br.com.stilldistribuidora.partners.Models.CabinAudioUploadFile;
import br.com.stilldistribuidora.partners.Repository.EntitySqlite.CabinAudioEntitySqlite;
import br.com.stilldistribuidora.partners.Repository.EntitySqlite.CabinAudioOperationEntitySqlite;

public class CabinAudioOperationRepository {
    private final Context context;
    private final CabinAudioOperationEntitySqlite audioEntitySqlite;

    public CabinAudioOperationRepository(Context context) {
        this.audioEntitySqlite = new CabinAudioOperationEntitySqlite(context);
        this.context = context;
    }

    public long createAudio(CabinAudioUploadFile audio,int partener) {
        SQLiteDatabase db = this.audioEntitySqlite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_DELIVERY, audio.delivery_id);
        values.put(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_PARTNER, partener);
        values.put(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_AUDIO_PATH, audio.url);
        values.put(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_IS_AUDIO_CHECKED, 0);
        long newRowId = db.insert(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_NAME, null, values);
        return newRowId;
    }


    public boolean hasAudio(String codeDelivery, String codePartner) {

        SQLiteDatabase db = this.audioEntitySqlite.getReadableDatabase();
        try {
            String[] projection = {
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_ID,
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_DELIVERY,
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_AUDIO_PATH
            };
            String selection =  CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_PARTNER + "=? and "+
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_DELIVERY + "=? ";
            String[] selectionArgs = {codePartner,codeDelivery};
            String sortOrder = null;
            Cursor cursor = db.query(
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
            if(cursor.moveToNext()) return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    public CabinAudioUploadFile audioBy(String codeDelivery, String codePartner) {
        CabinAudioUploadFile cabinAudioUploadFile = null;
        SQLiteDatabase db = this.audioEntitySqlite.getReadableDatabase();
        try {
            String[] projection = {
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_ID,
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_DELIVERY,
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_AUDIO_PATH
            };
            String selection =  CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_PARTNER + "=? and "+
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_DELIVERY + "=? ";
            String[] selectionArgs = {codePartner,codeDelivery};
            String sortOrder = null;
            Cursor cursor = db.query(
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
            if (cursor.moveToNext()) {
                cabinAudioUploadFile = new CabinAudioUploadFile();
                cabinAudioUploadFile.url = (cursor.getString(cursor.getColumnIndexOrThrow(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_AUDIO_PATH)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cabinAudioUploadFile;

    }



    public boolean updateCheckAudio(String deliveryCode, String partnerCode) {
        int resultDelete = 0;
        try {
            SQLiteDatabase db = this.audioEntitySqlite.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_IS_AUDIO_CHECKED,"S");

            resultDelete = db.update(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_NAME,
                    values,
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_DELIVERY+"=?"+
                     "and  "+CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_PARTNER+"=?",
                     new String[]{deliveryCode,partnerCode});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (resultDelete > 0) ? true : false;
    }


    public boolean hasCheckedAudio(String deliveryCode, String partnerCode) {
        int resultDelete = 0;
        try {
            SQLiteDatabase db = this.audioEntitySqlite.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_IS_AUDIO_CHECKED,"S");

            resultDelete = db.update(CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_NAME,
                    values,
                    CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_DELIVERY+"=?"+
                            " and  "+CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_PARTNER+"=?"+
                            " and "+CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_IS_AUDIO_CHECKED+"='S'",
                    new String[]{deliveryCode,partnerCode});
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (resultDelete > 0) ? true : false;
    }


}
