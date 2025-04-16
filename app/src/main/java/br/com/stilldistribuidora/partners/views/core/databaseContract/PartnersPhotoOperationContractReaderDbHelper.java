package br.com.stilldistribuidora.partners.views.core.databaseContract;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.stilldistribuidora.partners.views.core.contract.partnersPhotosOperationContract;

public class PartnersPhotoOperationContractReaderDbHelper extends SQLiteOpenHelper {
    protected  SQLiteDatabase db ;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "partnersPhotos.db";

    public PartnersPhotoOperationContractReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(partnersPhotosOperationContract.SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(partnersPhotosOperationContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
