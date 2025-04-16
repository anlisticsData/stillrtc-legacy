package br.com.stilldistribuidora.partners.Repository.EntitySqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import br.com.stilldistribuidora.partners.Repository.Audio.AudioCreateTable;
import br.com.stilldistribuidora.partners.Repository.Audio.CabinAudioOperationCreateTable;

public class CabinAudioOperationEntitySqlite extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME ="cabinSoundOperationDatabase.db";
    public CabinAudioOperationEntitySqlite(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CabinAudioOperationCreateTable.create());
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CabinAudioOperationCreateTable.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }



}
