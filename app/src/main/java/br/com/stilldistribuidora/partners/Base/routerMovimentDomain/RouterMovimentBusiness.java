package br.com.stilldistribuidora.partners.Base.routerMovimentDomain;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouterMovimentBusiness {
    private final RouterMovimentHelper routerMovimentHelper;

    public RouterMovimentBusiness(RouterMovimentHelper routerMovimentHelper) {
        this.routerMovimentHelper = routerMovimentHelper;
    }


    public boolean insert(RouterMovimentModel moviment) {
        long insert = 0;
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            SQLiteDatabase db = routerMovimentHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_CREATED_AT,dateFormat.format(date));
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_KEYS, moviment.getIdKeys());
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_GEO, moviment.getStartPoints());
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC, "0");
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_STATE_ACTION,moviment.getAction());
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DEVICE_ID ,moviment.getDeviceID());
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID ,moviment.getDeliveryId());
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_LAP ,moviment.getLap());
            insert = db.insert(EntitiesRouterMovimentApp.Columns.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insert > 0;
    }


    public boolean updateStateSyncOk(RouterMovimentModel moviment) {
        long update = 0;
        try {
            SQLiteDatabase db = routerMovimentHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC,"1");
            String selection = EntitiesRouterMovimentApp.Columns.COLUMN_KEYS + "=?";
            String[] selectionArgs = {moviment.getId()};
            update = db.update(
                    EntitiesRouterMovimentApp.Columns.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return update > 0;
    }


    public boolean updateStateSyncNot(RouterMovimentModel moviment) {
        long update = 0;
        try {
            SQLiteDatabase db = routerMovimentHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC,"0");
            String selection = EntitiesRouterMovimentApp.Columns.COLUMN_ID + "=?";
            String[] selectionArgs = {moviment.getId()};
            update = db.update(
                    EntitiesRouterMovimentApp.Columns.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return update > 0;
    }


    public List<RouterMovimentModel> getAllLimit(String state,String limit) {
        List<RouterMovimentModel> moviments = new ArrayList<>();
        try {

            String[] columns = {
                    EntitiesRouterMovimentApp.Columns.COLUMN_ID,
                    EntitiesRouterMovimentApp.Columns.COLUMN_CREATED_AT,
                    EntitiesRouterMovimentApp.Columns.COLUMN_KEYS,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_GEO,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_STATE_ACTION,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DEVICE_ID,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_LAP

            };
            String selection = EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC + "=?";
            String[] selectionArgs = {state};
            SQLiteDatabase db = routerMovimentHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    EntitiesRouterMovimentApp.Columns.TABLE_NAME,   // The table to query
                    columns,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null,               // The sort order
                    limit
            );

            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ID));
                String created_at = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_CREATED_AT));
                String keys = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_KEYS));
                String startPoint = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_GEO));
                String sync = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC));
                String action = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_STATE_ACTION));
                String deviceId = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DEVICE_ID));
                String deliveryId = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID));
                String lap = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_LAP));
                moviments.add(new RouterMovimentModel(itemId, created_at, keys, startPoint, sync,action,deviceId,deliveryId));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviments;
    }


    public List<RouterMovimentModel> getAllSynchronizeByOperation(String operation) {
        List<RouterMovimentModel> moviments = new ArrayList<>();
        try {

            String[] columns = {
                    EntitiesRouterMovimentApp.Columns.COLUMN_ID,
                    EntitiesRouterMovimentApp.Columns.COLUMN_CREATED_AT,
                    EntitiesRouterMovimentApp.Columns.COLUMN_KEYS,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_GEO,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_STATE_ACTION,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DEVICE_ID,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID

            };
            String selection = EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID + "=?";
            String[] selectionArgs = {operation};
            SQLiteDatabase db = routerMovimentHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    EntitiesRouterMovimentApp.Columns.TABLE_NAME,   // The table to query
                    columns,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ID));
                String created_at = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_CREATED_AT));
                String keys = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_KEYS));
                String startPoint = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_GEO));
                String sync = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC));
                String action = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_STATE_ACTION));
                String deviceId = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DEVICE_ID));
                String deliveryId = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID));
                moviments.add(new RouterMovimentModel(itemId, created_at, keys, startPoint, sync,action,deviceId,deliveryId));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviments;
    }



    public List<RouterMovimentModel> getAll(String state) {
        List<RouterMovimentModel> moviments = new ArrayList<>();
        try {

            String[] columns = {
                    EntitiesRouterMovimentApp.Columns.COLUMN_ID,
                    EntitiesRouterMovimentApp.Columns.COLUMN_CREATED_AT,
                    EntitiesRouterMovimentApp.Columns.COLUMN_KEYS,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_GEO,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_STATE_ACTION,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DEVICE_ID,
                    EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID

            };
            String selection = EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC + "=?";
            String[] selectionArgs = {state};
            SQLiteDatabase db = routerMovimentHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    EntitiesRouterMovimentApp.Columns.TABLE_NAME,   // The table to query
                    columns,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ID));
                String created_at = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_CREATED_AT));
                String keys = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_KEYS));
                String startPoint = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_GEO));
                String sync = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_SYNC));
                String action = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_STATE_ACTION));
                String deviceId = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DEVICE_ID));
                String deliveryId = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesRouterMovimentApp.Columns.COLUMN_ACTIVE_DELIVERY_ID));
                moviments.add(new RouterMovimentModel(itemId, created_at, keys, startPoint, sync,action,deviceId,deliveryId));
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviments;
    }
    public void close() {
        this.routerMovimentHelper.close();
    }
}

