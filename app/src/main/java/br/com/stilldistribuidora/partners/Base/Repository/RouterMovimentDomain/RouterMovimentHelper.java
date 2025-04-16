package br.com.stilldistribuidora.partners.Base.Repository.RouterMovimentDomain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class RouterMovimentHelper  extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RouterMovimentHelper.db";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EntitiesRouterMovimentApp.Columns.TABLE_NAME;




    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EntitiesRouterMovimentApp.Columns.TABLE_NAME + "(" +
                    "" + EntitiesRouterMovimentApp.Columns.COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " " + EntitiesRouterMovimentApp.Columns.COLUMN_CREATED_AT + "  TEXT NOT NULL," +
                    " " + EntitiesRouterMovimentApp.Columns.COLUMN_KEYS + "  TEXT NOT NULL," +
                    " " + EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_GEO + " TEXT,"+
                    " " + EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC + " TEXT,"+
                    " " + EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_STATE_ACTION + " TEXT,"+
                    " " + EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DEVICE_ID + " TEXT," +
                    " " + EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID + " TEXT)";


     public RouterMovimentHelper(@Nullable Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {

         db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }




}
