package br.com.stilldistribuidora.partners.Repository.RepositoryConcret;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.common.CastResponseApi;
import br.com.stilldistribuidora.partners.Commom.Enum;
import br.com.stilldistribuidora.partners.Repository.Contracts.AppOpeationInterface;
import br.com.stilldistribuidora.partners.Repository.Contracts.ITEntity;
import br.com.stilldistribuidora.partners.Repository.EntitySqlite.AppOpeationEntitySqlite;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.Repository.SqlCreateds.AppOpeationCreatedSql;
import br.com.stilldistribuidora.stillrtc.interfac.OnApiResponse;

public class AppOpeationsRepository implements AppOpeationInterface {
    private final Context context;
    private final AppOpeationEntitySqlite appOpeationEntity;

    public AppOpeationsRepository(Context context) {
        this.appOpeationEntity = new AppOpeationEntitySqlite(context);
        this.context = context;
    }


    @Override
    public long create(ITEntity TEntity) {


        OperationModelRepository operationModelRepository = (OperationModelRepository) TEntity;
        OperationModelRepository by = (OperationModelRepository) this.getBy(operationModelRepository);
        if (by.getOperationID() != null) {
            return -1;
        }

        SQLiteDatabase db = this.appOpeationEntity.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID, operationModelRepository.getOperationID());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE, operationModelRepository.type());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID, operationModelRepository.getRouterID());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID, operationModelRepository.getParternID());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID, operationModelRepository.getStoreID());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE, 0);
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP, operationModelRepository.getRouterMap());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON, operationModelRepository.getZonasJson());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON, operationModelRepository.getRouterJson());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART, operationModelRepository.getPontStart());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME, operationModelRepository.getStorename());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT, operationModelRepository.getCreatedAt());
        values.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NUVEM, 0);
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME, null, values);


        return newRowId;
    }

    @Override
    public ITEntity getBy(ITEntity filter) {
        OperationModelRepository operationModelRepository = (OperationModelRepository) filter;
        OperationModelRepository entity = null;
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] projection = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT


            };
            // Filter results WHERE "title" = 'My Title'
            String selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID + " = ?";
            String[] selectionArgs = {String.valueOf(operationModelRepository.getOperationID())};
            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            cursor.moveToNext();
            entity = new OperationModelRepository();
            entity.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID)));
            entity.setOperationID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID)));
            entity.setRouterID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID)));
            entity.setStoreID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID)));
            entity.setParternID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID)));
            entity.setState((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE)));
            entity.setDistanceKm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM)));
            entity.setDistanceMm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM)));
            entity.setZonasJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON)));
            entity.setRouterJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON)));
            entity.setRouterMap(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP)));
            entity.setStorename(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME)));
            entity.setPontStart(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART)));
            entity.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT)));
            entity.setUpdateAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT)));
            cursor.close();
        } catch (Exception e) {
        }
        return entity;
    }




    public List<OperationModelRepository> getAllPartnerId(String partnerId, String state) {

        List<OperationModelRepository> entitys = new ArrayList<>();
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] projection = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_JUSTIFICATIONS,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT


            };
            String selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "= ?   " +
                    " and " + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE + "=? and nuvem=0";
            String[] selectionArgs = {partnerId, state};
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while (cursor.moveToNext()) {


                OperationModelRepository entity = new OperationModelRepository();
                entity.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID)));
                entity.setOperationID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID)));
                entity.setRouterID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID)));
                entity.setStoreID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID)));
                entity.setParternID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID)));
                entity.setState((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE)));
                entity.setDistanceKm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM)));
                entity.setDistanceMm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM)));
                entity.setZonasJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON)));
                entity.setRouterJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON)));
                entity.setRouterMap(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP)));
                entity.setStorename(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME)));
                entity.setPontStart(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART)));
                entity.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT)));
                entity.setUpdateAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT)));
                entity.setJustification(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_JUSTIFICATIONS)));

                entitys.add(entity);

            }
            cursor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return entitys;
    }




    public List<String> bringAllPartnerOperationId(String partnerId) {


        List<String> deliveryIds = new ArrayList<>();
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] rows_selected = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID,

            };
            String selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "= ?     ";
            selection+= " and "+ AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE +"=? ";


            String[] selectionArgs = {partnerId, String.valueOf(Enum.STATUS_ACCEPTED)};
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    rows_selected,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while (cursor.moveToNext()) {
                deliveryIds.add( cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID)));
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return deliveryIds;
    }




    public List<OperationModelRepository> hasOpenOperation(String partnerId) {
        String state="6";
        List<OperationModelRepository> entitys = new ArrayList<>();
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] projection = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_JUSTIFICATIONS,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT


            };
            String selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "= ?   " +
                    " and " + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE + "=?";
            String[] selectionArgs = {partnerId, state};
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while (cursor.moveToNext()) {


                OperationModelRepository entity = new OperationModelRepository();
                entity.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID)));
                entity.setOperationID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID)));
                entity.setRouterID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID)));
                entity.setStoreID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID)));
                entity.setParternID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID)));
                entity.setState((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE)));
                entity.setDistanceKm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM)));
                entity.setDistanceMm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM)));
                entity.setZonasJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON)));
                entity.setRouterJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON)));
                entity.setRouterMap(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP)));
                entity.setStorename(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME)));
                entity.setPontStart(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART)));
                entity.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT)));
                entity.setUpdateAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT)));
                entity.setJustification(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_JUSTIFICATIONS)));

                entitys.add(entity);

            }
            cursor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return entitys;
    }


    public List<OperationModelRepository> getAllPartnerId(String partnerId, String data, String state) {

        List<OperationModelRepository> entitys = new ArrayList<>();
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] projection = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT


            };

            String selection = "";

            String[] selectionArgs=null;
            if (state.equals(String.valueOf(Enum.STATUS_INVALID_OPERATION_STATUS))) {
                selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + " = ? and " +
                        " " + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT + " like ?  and   "+
                        " " + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION + "="+Enum.STATUS_ACCEPTED_SAVE_DEVICE;
                selectionArgs = new String[]{String.valueOf(partnerId), "" + data + "%"};


            }else if (state.equals("6")) {
                selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + " = ? and " +
                        " " + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE + "="+state;
                selectionArgs = new String[]{String.valueOf(partnerId)};

            }else {
                selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + " = ? and " +
                        " " + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT + " like ? " +
                        " and  (" + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE + "=?  "+
                        " and  " + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION + "=0 or " +
                        "  " + AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION + " is null )  ";
                selectionArgs =new String[]{String.valueOf(partnerId), "" + data + "%", state};
            }


            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            while (cursor.moveToNext()) {
                OperationModelRepository entity = new OperationModelRepository();
                entity.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID)));
                entity.setOperationID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID)));
                entity.setRouterID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID)));
                entity.setTypeOperation((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE)));
                entity.setStoreID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID)));
                entity.setParternID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID)));
                entity.setState((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE)));
                entity.setDistanceKm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM)));
                entity.setDistanceMm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM)));
                entity.setZonasJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON)));
                entity.setRouterJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON)));
                entity.setRouterMap(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP)));
                entity.setStorename(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME)));
                entity.setPontStart(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART)));
                entity.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT)));
                entity.setUpdateAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT)));
                entity.setAcceptOperation(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION)));

                entitys.add(entity);

            }
            cursor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return entitys;
    }




    public List<OperationModelRepository> getAllAcceptedNuvem() {

        List<OperationModelRepository> entitys = new ArrayList<>();
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] projection = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT


            };
            String selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION_UPDATE + " = ?";
            String[] selectionArgs = {String.valueOf('N')};
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            while (cursor.moveToNext()) {
                OperationModelRepository entity = new OperationModelRepository();
                entity.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID)));
                entity.setOperationID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID)));
                entity.setRouterID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID)));
                entity.setTypeOperation((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE)));
                entity.setStoreID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID)));
                entity.setParternID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID)));
                entity.setState((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE)));
                entity.setDistanceKm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM)));
                entity.setDistanceMm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM)));
                entity.setZonasJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON)));
                entity.setRouterJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON)));
                entity.setRouterMap(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP)));
                entity.setStorename(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME)));
                entity.setPontStart(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART)));
                entity.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT)));
                entity.setUpdateAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT)));

                entitys.add(entity);

            }
            cursor.close();
        } catch (Exception e) {
        }
        return entitys;
    }



    public List<OperationModelRepository> getAllAccepted() {

        List<OperationModelRepository> entitys = new ArrayList<>();
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] projection = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT


            };
            // Filter results WHERE "title" = 'My Title'
            String selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION + " = ?";
            String[] selectionArgs = {String.valueOf('1')};
            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            while (cursor.moveToNext()) {
                OperationModelRepository entity = new OperationModelRepository();
                entity.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID)));
                entity.setOperationID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID)));
                entity.setTypeOperation((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE)));
                entity.setRouterID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID)));
                entity.setStoreID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID)));
                entity.setParternID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID)));
                entity.setState((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE)));
                entity.setDistanceKm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM)));
                entity.setDistanceMm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM)));
                entity.setZonasJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON)));
                entity.setRouterJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON)));
                entity.setRouterMap(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP)));
                entity.setStorename(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME)));
                entity.setPontStart(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART)));
                entity.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT)));
                entity.setUpdateAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT)));

                entitys.add(entity);

            }
            cursor.close();
        } catch (Exception e) {
        }
        return entitys;
    }






    public List<OperationModelRepository> getAll(String state) {

        List<OperationModelRepository> entitys = new ArrayList<>();
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] projection = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT,
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT


            };
            // Filter results WHERE "title" = 'My Title'
            String selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE + " = ?";
            String[] selectionArgs = {String.valueOf(state)};
            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            while (cursor.moveToNext()) {
                OperationModelRepository entity = new OperationModelRepository();
                entity.setId((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID)));
                entity.setOperationID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID)));
                entity.setTypeOperation((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE)));
                entity.setRouterID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID)));
                entity.setStoreID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STOREID)));
                entity.setParternID((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID)));
                entity.setState((int) cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE)));
                entity.setDistanceKm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM)));
                entity.setDistanceMm(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM)));
                entity.setZonasJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ZONASJSON)));
                entity.setRouterJson(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON)));
                entity.setRouterMap(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP)));
                entity.setStorename(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STORENAME)));
                entity.setPontStart(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PONTSTART)));
                entity.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_CREATED_AT)));
                entity.setUpdateAt(cursor.getString(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT)));

                entitys.add(entity);

            }
            cursor.close();
        } catch (Exception e) {
        }
        return entitys;
    }


    public String getAllToPipeOperationsSpareted(String state) {

        List<String> operations = new ArrayList<>();
        SQLiteDatabase db = this.appOpeationEntity.getReadableDatabase();
        try {
            String[] projection = {
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID

            };
            // Filter results WHERE "title" = 'My Title'
            String selection = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE + " = ?";
            String[] selectionArgs = {String.valueOf(state)};
            // How you want the results sorted in the resulting Cursor
            String sortOrder = null;
            Cursor cursor = db.query(
                    AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );
            while (cursor.moveToNext()) {


                operations.add(String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID))));


            }
            cursor.close();
        } catch (Exception e) {
        }
        return String.join("|", operations);
    }


    public void finalizePartnerTransaction(String operationId, String partnerId, String justifications, OnApiResponse onApiResponse) {

        long update = 0;
        try {
            ContentValues valores;
            String where;
            SQLiteDatabase db = this.appOpeationEntity.getWritableDatabase();
            where = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID + "=" + operationId;
            where += " and ";
            where += AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "=" + partnerId;
            valores = new ContentValues();
            valores.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_JUSTIFICATIONS, String.valueOf(justifications));
            valores.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE, Enum.STATUS_CANCELED_SAVE_DEVICE);
            update = db.update(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME, valores, where, null);
            db.close();
            CastResponseApi castResponseApi = new CastResponseApi();
            castResponseApi.setMensage("Operação Finalizado com Sucesso.");
            castResponseApi.setState(200);
            onApiResponse.onSucess(castResponseApi);
        } catch (Exception e) {
            CastResponseApi castResponseApi = new CastResponseApi();
            castResponseApi.setMensage(e.getMessage());
            castResponseApi.setState(202);
            onApiResponse.onError(castResponseApi);
        }

    }


    public long updateStateOperations(String operationId, String partnerId, int state) {
        long update = 0;
        try {
            ContentValues valores;
            String where;
            SQLiteDatabase db = this.appOpeationEntity.getWritableDatabase();
            where = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID + "=" + operationId;
            where += " and ";
            where += AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "=" + partnerId;
            valores = new ContentValues();
            valores.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE, String.valueOf(state));
            update = db.update(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME, valores, where, null);
            db.close();

        } catch (Exception e) {
        }
        return update;
    }

    public long updateStateOperationsNuvem(String operationId, String partnerId, int state,int nuvem) {
        long update = 0;
        try {
            ContentValues valores;
            String where;
            SQLiteDatabase db = this.appOpeationEntity.getWritableDatabase();
            where = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID + "=" + operationId;
            where += " and ";
            where += AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "=" + partnerId;
            valores = new ContentValues();
            valores.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_STATE, String.valueOf(state));
            valores.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NUVEM, String.valueOf(nuvem));
            update = db.update(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME, valores, where, null);
            db.close();

        } catch (Exception e) {
        }
        return update;
    }



    public long uploadAcceptionoperationNuvem(String id,String operationId, String partnerId,String routerId) {
        long update = 0;
        try {
            ContentValues valores;
            String where;
            SQLiteDatabase db = this.appOpeationEntity.getWritableDatabase();
            where = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID + "=" + operationId;
            where += " and ";
            where += AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "=" + partnerId;
            where += " and ";
            where += AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ID + "=" + id;

            valores = new ContentValues();
            valores.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION_UPDATE,"S");
            update = db.update(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME, valores, where, null);
            db.close();
        } catch (Exception e) {
        }
        return update;
    }



    public long setAcceptionoperation(String operationId, String partnerId,String acception) {
        long update = 0;
        try {
            ContentValues valores;
            String where;
            SQLiteDatabase db = this.appOpeationEntity.getWritableDatabase();
            where = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID + "=" + operationId;
            where += " and ";
            where += AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "=" + partnerId;
            valores = new ContentValues();
            valores.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION,acception);
            update = db.update(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME, valores, where, null);
            db.close();
        } catch (Exception e) {
        }
        return update;
    }


    public long setAcceptionoperationUpdate(String operationId, String partnerId,String acception) {
        long update = 0;
        try {
            ContentValues valores;
            String where;
            SQLiteDatabase db = this.appOpeationEntity.getWritableDatabase();
            where = AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID + "=" + operationId;
            where += " and ";
            where += AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_PARTERNID + "=" + partnerId;
            valores = new ContentValues();
            valores.put(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION_UPDATE,acception);
            update = db.update(AppOpeationCreatedSql.AppOpeationCreatedSqlEntry.TABLE_NAME, valores, where, null);
            db.close();
        } catch (Exception e) {
        }
        return update;
    }





}
