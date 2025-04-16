package br.com.stilldistribuidora.partners.Base.Repository.RoutesDomain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class OperationsBusiness {
    private final OperationsHelper OperationsModel;

    public OperationsBusiness(OperationsHelper OperationsModel) {
        this.OperationsModel = OperationsModel;
    }

    public boolean insert(RoutesModel conf) {
        long insert = 0;
        try {
            if (this.getByUuidOpetion(String.valueOf(conf.getIdrotas())) == null) {
                List<RoutesCoods> coods = conf.getCoods();
                StringBuffer textCoods = new StringBuffer();
                for (RoutesCoods rt : coods) {
                    textCoods.append(rt.getLatitude() + "," + rt.getLonguitude() + "," + rt.getDirections() + "|");
                }
                SQLiteDatabase db = OperationsModel.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(EntitiesOperationsApp.Operations.COLUMN_CREATED_AT, conf.getCreated_at());
                values.put(EntitiesOperationsApp.Operations.COLUMN_ID_ROTAS, conf.getIdrotas());
                values.put(EntitiesOperationsApp.Operations.COLUMN_ROUTES, textCoods.toString());
                values.put(EntitiesOperationsApp.Operations.COLUMN_ROUTES_KEYS, conf.getKeis());
                values.put(EntitiesOperationsApp.Operations.COLUMN_ACTIVE_DEVICE_ID, conf.getDeviceId());


                insert = db.insert(EntitiesOperationsApp.Operations.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insert > 0;
    }


    public RoutesModel getByUuidOpetion(String uuidConfig) {
        RoutesModel routesModel = null;
        try {
            String[] columns = {
                    EntitiesOperationsApp.Operations.COLUMN_ID,
                    EntitiesOperationsApp.Operations.COLUMN_CREATED_AT,
                    EntitiesOperationsApp.Operations.COLUMN_ID_ROTAS,
                    EntitiesOperationsApp.Operations.COLUMN_ROUTES_KEYS,
                    EntitiesOperationsApp.Operations.COLUMN_ACTIVE_DEVICE_ID
            };
            String selection = EntitiesOperationsApp.Operations.COLUMN_ID_ROTAS + " = ?";
            String[] selectionArgs = {uuidConfig};
            SQLiteDatabase db = OperationsModel.getReadableDatabase();
            Cursor cursor = db.query(
                    EntitiesOperationsApp.Operations.TABLE_NAME,   // The table to query
                    columns,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );
            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ID));
                String created_at = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_CREATED_AT));
                int idrota = cursor.getInt(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ID_ROTAS));
                String keys = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ROUTES_KEYS));
                String deviceId = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ACTIVE_DEVICE_ID));
                routesModel = new RoutesModel(itemId, created_at, idrota, "", keys);
                routesModel.setDeviceId(deviceId);


            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routesModel;
    }


    public boolean delete() {
        long delete = 0;
        try {
            SQLiteDatabase db = OperationsModel.getReadableDatabase();
            String selection = EntitiesOperationsApp.Operations.COLUMN_ID + " > ?";
            String[] selectionArgs = {"0"};
            delete = db.delete(EntitiesOperationsApp.Operations.TABLE_NAME, selection, selectionArgs);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return (delete > 0);
    }


    public List<RoutesModel> getAll() {
        List<RoutesModel> configurations = new ArrayList<>();
        try {

            String[] columns = {
                    EntitiesOperationsApp.Operations.COLUMN_ID,
                    EntitiesOperationsApp.Operations.COLUMN_ROUTES,
                    EntitiesOperationsApp.Operations.COLUMN_CREATED_AT,
                    EntitiesOperationsApp.Operations.COLUMN_ID_ROTAS,
                    EntitiesOperationsApp.Operations.COLUMN_ROUTES_KEYS,
                    EntitiesOperationsApp.Operations.COLUMN_ACTIVE_DEVICE_ID

            };
            String selection = null;
            String[] selectionArgs = null;
            SQLiteDatabase db = OperationsModel.getReadableDatabase();
            Cursor cursor = db.query(
                    EntitiesOperationsApp.Operations.TABLE_NAME,   // The table to query
                    columns,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ID));

                String created_at = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_CREATED_AT));
                int idrota = cursor.getInt(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ID_ROTAS));
                String rotas = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ROUTES));
                String keys = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ROUTES_KEYS));
                String deviceId = cursor.getString(cursor.getColumnIndexOrThrow(EntitiesOperationsApp.Operations.COLUMN_ACTIVE_DEVICE_ID));
                RoutesModel rtm = new RoutesModel(itemId, created_at, idrota, rotas);
                rtm.setKeis(keys);
                rtm.setDeviceId(deviceId);

                configurations.add(rtm);

            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configurations;
    }


    public void close() {
        this.OperationsModel.close();
    }


}
