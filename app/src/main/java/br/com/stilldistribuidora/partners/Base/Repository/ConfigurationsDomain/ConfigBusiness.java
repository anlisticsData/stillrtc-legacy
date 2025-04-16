package br.com.stilldistribuidora.partners.Base.Repository.ConfigurationsDomain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ConfigBusiness {
    private final ConfigurationsDbHelper configurationModel;

    public ConfigBusiness(ConfigurationsDbHelper configurationModel) {
        this.configurationModel = configurationModel;
    }

    public boolean insert(ConfigurationModel conf) {
        long insert = 0;
        try {
            if (this.getByUuidConfig(conf.getUuu_config()) == null) {
                SQLiteDatabase db = configurationModel.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG, conf.getUuu_config());
                values.put(EntitiesConfigurationsApp.Config.COLUMN_CONTENT, conf.getContent());
                insert = db.insert(EntitiesConfigurationsApp.Config.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insert > 0;
    }


    public ConfigurationModel getByUuidConfig(String uuidConfig) {
        ConfigurationModel configuration = null;
        try {
            String[] columns = {
                    EntitiesConfigurationsApp.Config.COLUMN_ID,
                    EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG,
                    EntitiesConfigurationsApp.Config.COLUMN_CONTENT
            };
            String selection = EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG + " = ?";
            String[] selectionArgs = {uuidConfig};
            SQLiteDatabase db = configurationModel.getReadableDatabase();
            Cursor cursor = db.query(
                    EntitiesConfigurationsApp.Config.TABLE_NAME,   // The table to query
                    columns,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );
            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(EntitiesConfigurationsApp.Config.COLUMN_ID));
                String uuid_config = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesConfigurationsApp.Config.COLUMN_CONTENT));
                configuration = new ConfigurationModel(itemId, uuid_config, content);

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configuration;
    }


    public List<ConfigurationModel> getAll() {
        List<ConfigurationModel> configurations = new ArrayList<>();
        try {

            String[] columns = {
                    EntitiesConfigurationsApp.Config.COLUMN_ID,
                    EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG,
                    EntitiesConfigurationsApp.Config.COLUMN_CONTENT
            };
            String selection = null;
            String[] selectionArgs = null;
            SQLiteDatabase db = configurationModel.getReadableDatabase();
            Cursor cursor = db.query(
                    EntitiesConfigurationsApp.Config.TABLE_NAME,   // The table to query
                    columns,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(EntitiesConfigurationsApp.Config.COLUMN_ID));
                String uuid_config = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesConfigurationsApp.Config.COLUMN_CONTENT));
                configurations.add(new ConfigurationModel(itemId, uuid_config, content));

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configurations;
    }


    public boolean update(ConfigurationModel conf) {
        long update = 0;
        try {
            SQLiteDatabase db = configurationModel.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EntitiesConfigurationsApp.Config.COLUMN_CONTENT, conf.getContent());
            String selection = EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG + " = ?";
            String[] selectionArgs = {conf.getUuu_config()};
            update = db.update(
                    EntitiesConfigurationsApp.Config.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return update > 0;
    }
    public boolean delete(String uuidConfig ) {
        long delete = 0;
        try {
            SQLiteDatabase db = configurationModel.getWritableDatabase();
            String selection = EntitiesConfigurationsApp.Config.COLUMN_UUID_CONFIG + " = ?";
            String[] selectionArgs = {uuidConfig};
            delete = db.delete(
                    EntitiesConfigurationsApp.Config.TABLE_NAME,
                    selection,
                    selectionArgs);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return delete > 0;
    }




    public void close(){
        this.configurationModel.close();
    }


}
