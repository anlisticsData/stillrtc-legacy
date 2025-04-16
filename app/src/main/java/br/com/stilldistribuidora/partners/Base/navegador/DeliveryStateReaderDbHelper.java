package br.com.stilldistribuidora.partners.Base.navegador;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DeliveryStateReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DeliveState.db";

    public DeliveryStateReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DeliveryStateConf.SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DeliveryStateConf.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insert(DeliveryStateModel operationState) {

        if(this.getAllDeliveryLatLngBy(operationState.getDeliveryid(),operationState.getLatlng())){
            return -1;
        }
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_UUIDCOMPOSTA, operationState.getUuidcomposta());
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID, operationState.getDeliveryid());
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_ROUTERID, operationState.getRouterid());
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_STOREID, operationState.getStoreid());
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_PARTNERID, operationState.getPartnerid());
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG, operationState.getLatlng());
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID, operationState.getDeviceid());
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_BATERY, operationState.getBatery());
        values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_LAPS, operationState.getLaps());
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(DeliveryState.DeliveryStateEntry.TABLE_NAME, null, values);
        return newRowId;
    }


    public boolean getAllDeliveryLatLngBy(String deliveryId,String latlng) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID,
        };


        String selection = DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID + "=? and "+
                           DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG+"=?";

        String[] selectionArgs = {deliveryId,latlng};
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID + " asc";
        Cursor cursor = db.query(
                DeliveryState.DeliveryStateEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder,              // The sort order
                null
        );

        if(cursor.moveToNext()){
            return true;
        }
        return false;

    }


    public int deleted(String props) {


      /*  SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = Navegation.NavegationEntry.COLUMN_NAME_PROPS + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {props};
        // Issue SQL statement.
        int deletedRows = db.delete(Navegation.NavegationEntry.TABLE_NAME, selection, selectionArgs);
        return deletedRows;*/
        return 0;
    }


    public List<DeliveryStateModel> getNuvemLimit(int limit) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_UUIDCOMPOSTA,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_PARTNERID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_STOREID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ROUTERID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_BATERY,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_LAPS

        };


        String selection = DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM + "=?";
        String[] selectionArgs = {"0"};
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID + " asc";
        Cursor cursor = db.query(
                DeliveryState.DeliveryStateEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder,              // The sort order
                String.valueOf(limit)
        );
        List<DeliveryStateModel> itens = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {


                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID));
                String uuidcomposta = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_UUIDCOMPOSTA));
                String deliveryid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID));
                String routerid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_ROUTERID));
                String storeid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_STOREID));
                String partnerid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_PARTNERID));
                String latlng = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG));
                String nuvem = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM));
                String deviceid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID));
                String batery = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_BATERY));
                String laps = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_LAPS));


                DeliveryStateModel delivery = new DeliveryStateModel();
                delivery.setId(itemId);
                delivery.setUuidcomposta(uuidcomposta);
                delivery.setDeliveryid(deliveryid);
                delivery.setRouterid(Integer.valueOf(routerid));
                delivery.setStoreid(Integer.valueOf(storeid));
                delivery.setPartnerid(Integer.valueOf(partnerid));
                delivery.setNuvem(1);
                delivery.setLatlng(latlng);
                delivery.setDeviceid(deviceid);
                delivery.setBatery(batery);
                delivery.setLaps(Integer.parseInt(laps));
                itens.add(delivery);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        cursor.close();
        return itens;
    }


    public List<DeliveryStateModel> getAllDeliveryBy(String deliveryId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_UUIDCOMPOSTA,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_PARTNERID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_STOREID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ROUTERID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_BATERY

        };


        String selection = DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID + "=?";
        String[] selectionArgs = {deliveryId};
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID + " asc";
        Cursor cursor = db.query(
                DeliveryState.DeliveryStateEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder,              // The sort order
                null
        );
        List<DeliveryStateModel> itens = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {


                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID));
                String uuidcomposta = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_UUIDCOMPOSTA));
                String deliveryid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID));
                String routerid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_ROUTERID));
                String storeid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_STOREID));
                String partnerid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_PARTNERID));
                String latlng = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG));
                String nuvem = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM));
                String deviceid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID));
                String batery = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_BATERY));

                DeliveryStateModel delivery = new DeliveryStateModel();
                delivery.setId(itemId);
                delivery.setUuidcomposta(uuidcomposta);
                delivery.setDeliveryid(deliveryid);
                delivery.setRouterid(Integer.valueOf(routerid));
                delivery.setStoreid(Integer.valueOf(storeid));
                delivery.setPartnerid(Integer.valueOf(partnerid));
                delivery.setNuvem(1);
                delivery.setLatlng(latlng);
                delivery.setDeviceid(deviceid);
                delivery.setBatery(batery);
                itens.add(delivery);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        cursor.close();
        return itens;
    }




    public List<DeliveryStateModel> getNuvem() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_UUIDCOMPOSTA,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_PARTNERID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_STOREID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ROUTERID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID,
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_BATERY

        };


        String selection = DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM + "=?";
        String[] selectionArgs = {"0"};
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID + " asc";
        Cursor cursor = db.query(
                DeliveryState.DeliveryStateEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        List<DeliveryStateModel> itens = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {


                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID));
                String uuidcomposta = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_UUIDCOMPOSTA));
                String deliveryid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID));
                String routerid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_ROUTERID));
                String storeid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_STOREID));
                String partnerid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_PARTNERID));
                String latlng = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG));
                String nuvem = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM));
                String deviceid = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID));
                String batery = cursor.getString(cursor.getColumnIndexOrThrow(DeliveryState.DeliveryStateEntry.COLUMN_NAME_BATERY));

                DeliveryStateModel delivery = new DeliveryStateModel();
                delivery.setId(itemId);
                delivery.setUuidcomposta(uuidcomposta);
                delivery.setDeliveryid(deliveryid);
                delivery.setRouterid(Integer.valueOf(routerid));
                delivery.setStoreid(Integer.valueOf(storeid));
                delivery.setPartnerid(Integer.valueOf(partnerid));
                delivery.setNuvem(1);
                delivery.setLatlng(latlng);
                delivery.setDeviceid(deviceid);
                delivery.setBatery(batery);
                itens.add(delivery);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        cursor.close();
        return itens;
    }


    public long updateStateSyncNot(DeliveryStateModel deliveryStateModel) {
        int count = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM, "0");
            String selection = DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID + "=?";
            String[] selectionArgs = {String.valueOf(deliveryStateModel.getId())};
            count = db.update(
                    DeliveryState.DeliveryStateEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        } catch (Exception e) {
        }
        return count;

    }


    public long update(DeliveryStateModel deliveryStateModel) {
        int count = 0;
        try {
            SQLiteDatabase db = getWritableDatabase();
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM, "1");
            String selection = DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID + "=?";
            String[] selectionArgs = {String.valueOf(deliveryStateModel.getId())};
            count = db.update(
                    DeliveryState.DeliveryStateEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return count;

    }
}