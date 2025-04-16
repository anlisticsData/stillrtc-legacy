package br.com.stilldistribuidora.partners.Base.Repository.RoutesDomain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OperationsHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Operations.db";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EntitiesOperationsApp.Operations.TABLE_NAME;


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EntitiesOperationsApp.Operations.TABLE_NAME + "(" +
                    "" + EntitiesOperationsApp.Operations.COLUMN_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " " + EntitiesOperationsApp.Operations.COLUMN_CREATED_AT + "  TEXT NOT NULL," +
                    " " + EntitiesOperationsApp.Operations.COLUMN_ROUTES + "  TEXT NOT NULL," +
                    " " + EntitiesOperationsApp.Operations.COLUMN_ID_ROTAS + "  INTEGER," +
                    " " + EntitiesOperationsApp.Operations.COLUMN_ACTIVE_DEVICE_ID + "  TEXT," +
                    " " + EntitiesOperationsApp.Operations.COLUMN_ROUTES_KEYS + " TEXT NOT NULL)";


    public OperationsHelper(Context context) {
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
