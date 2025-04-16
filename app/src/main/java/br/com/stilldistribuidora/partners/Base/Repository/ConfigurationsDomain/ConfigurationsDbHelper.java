package br.com.stilldistribuidora.partners.Base.Repository.ConfigurationsDomain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ConfigurationsDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Configurations.db";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EntitiesConfigurationsApp.Config.TABLE_NAME;


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ EntitiesConfigurationsApp.Config.TABLE_NAME+"(" +
            ""+ EntitiesConfigurationsApp.Config.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            " "+ EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG+"  TEXT NOT NULL," +
            " "+ EntitiesConfigurationsApp.Config.COLUMN_CONTENT+"  TEXT)";


    public ConfigurationsDbHelper(Context context) {
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
