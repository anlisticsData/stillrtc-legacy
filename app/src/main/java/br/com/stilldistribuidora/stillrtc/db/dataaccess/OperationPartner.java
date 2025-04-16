package br.com.stilldistribuidora.stillrtc.db.dataaccess;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.models.AvailableOperation;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.OperationPartnerModel;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

public class OperationPartner extends ProviderDataAccess {
    private final String nameDB = OperationPartner.class.getSimpleName() + ".db";
    private final String nameTbl = OperationPartner.class.getSimpleName();

    private boolean isTransaction = false;


    public OperationPartner(Context context) {
        this.context = context;
    }

    public OperationPartner(DBHelper db, boolean isTransaction) {
        this.db = db;
        this.isTransaction = isTransaction;
    }


    @Override
    public long insert(Object obj) {

        AvailableOperation operationSelected=(AvailableOperation) obj;
        long _id = 0;
        try {
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }
            ContentValues _values = new ContentValues();
            _values.put(Constants.PARTNER_OPERATION_ROUTER_ID   , operationSelected.getRouter_id());
            _values.put(Constants.PARTNER_OPERATION_ID, operationSelected.getOperation());
            _values.put(Constants.PARTNER_OPERATION_STORE_ID, operationSelected.getStore());
            _values.put(Constants.PARTNER_OPERATION_UUID_PARTNER, operationSelected.getIrPartrner());
            _values.put(Constants.PARTNER_OPERATION_CREATED_AT, operationSelected.getCreated());
            _values.put(Constants.PARTNER_OPERATION_STORE_NAME, operationSelected.getStoreName());
            _values.put(Constants.PARTNER_OPERATION_ROUTER_INSTRUCTIONS, operationSelected.getRouter_id_instructions());
            _values.put(Constants.PARTNER_OPERATION_PONT_START, operationSelected.getPointStart());
            _values.put(Constants.PARTNER_OPERATION_ADDRESS, operationSelected.getStartAddressInit());
            _values.put(Constants.PARTNER_OPERATION_DISTANCE_KM, operationSelected.getDistancekm());
            _values.put(Constants.PARTNER_OPERATION_DISTANCE_METERS, operationSelected.getDistanceM());
            _values.put(Constants.PARTNER_OPERATION_STATE_OPERATION, 1);
            _values.put(Constants.PARTNER_OPERATION_DEVICE_SELECIONADO, "");
            _id = db.getDb().insert(nameTbl, "", _values);
            return _id;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (!isTransaction)
                db.getDb().close();
        }
    }

    public long updateState(String state, String operationId) {

        long _id = 0;
        try {
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }
            ContentValues _values = new ContentValues();
            _values.put(Constants.PARTNER_OPERATION_STATE_OPERATION, state);


            _id = db.getDb().update(nameTbl,_values,Constants.PARTNER_OPERATION_ID+"=?",new String[]{operationId});
            return _id;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (!isTransaction)
                db.getDb().close();
        }

    }





    public long updateState(String state, String operationId,String idpartners,String deviceId) {

        long _id = 0;
        try {
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }
            ContentValues _values = new ContentValues();
            _values.put(Constants.PARTNER_OPERATION_STATE_OPERATION, state);
            _values.put(Constants.PARTNER_OPERATION_DEVICE_SELECIONADO, deviceId);

            _id = db.getDb().update(nameTbl,_values,Constants.PARTNER_OPERATION_ID+"=? and  "+Constants.PARTNER_OPERATION_UUID_PARTNER+"=?",new String[]{operationId,idpartners});
            return _id;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (!isTransaction)
                db.getDb().close();
        }

    }






    public long updateState(String state, String operationId,String deviceId) {

         long _id = 0;
         try {
             if (!isTransaction) {
                 db = new DBHelper(context, nameDB);
             }
             ContentValues _values = new ContentValues();
             _values.put(Constants.PARTNER_OPERATION_STATE_OPERATION, state);
             _values.put(Constants.PARTNER_OPERATION_DEVICE_SELECIONADO, deviceId);

             _id = db.getDb().update(nameTbl,_values,Constants.PARTNER_OPERATION_ID+"=?",new String[]{operationId});
             return _id;
         } catch (SQLException e) {
             throw new RuntimeException(e.getMessage());
         } finally {
             if (!isTransaction)
                 db.getDb().close();
         }

     }

    @Override
    public long update(Object obj, String where) {
        return 0;
    }

    @Override
    public void delete(String where) {

    }

    @Override
    public Cursor getCursor(String orderby, String where) {
        try {
            return db.getDb().query(nameTbl, OperationPartnerModel.columns, where, null, null, null, orderby);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getById(String where) {
        return null;
    }


    @SuppressLint("Range")
    public Object hasOperationPartnersInDabe(String delivery, String partner) {
        List<Object> _lista = new ArrayList<Object>();

        try {
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }

            String where=Constants.PARTNER_OPERATION_ID+"="+delivery+" and "+Constants.PARTNER_OPERATION_UUID_PARTNER+"="+partner;

            Cursor _c = getCursor(null,where);

            if (_c.moveToFirst()) {
                do {
                    AvailableOperation partneOperation = new AvailableOperation();
                    partneOperation.setIrPartrner(_c.getString(_c.getColumnIndex("uuid")));
                    _lista.add(partneOperation);
                } while (_c.moveToNext());
            }
            _c.close();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (!isTransaction) {
                db.getDb().close();
            }
        }

        return _lista.size();


    }


    @SuppressLint("Range")
    @Override
    public List<?> getList(String where, String orderby) {


        try {
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }
            Cursor _c = getCursor(orderby,where);
            List<Object> _lista = new ArrayList<Object>();
            if (_c.moveToFirst()) {
                do {
                    AvailableOperation partneOperation = new AvailableOperation();
                    partneOperation.setUuid(_c.getString(_c.getColumnIndex("uuid")));
                    partneOperation.setRouter_id(_c.getInt(_c.getColumnIndex("router_id")));
                    partneOperation.setOperation(_c.getString(_c.getColumnIndex("operation_id")));
                    partneOperation.setIdUserSelectonOperation(_c.getString(_c.getColumnIndex("id_user_selected_operation")));
                    partneOperation.setIrPartrner(_c.getString(_c.getColumnIndex("id_partner")));
                    partneOperation.setStore(_c.getString(_c.getColumnIndex("store_id")));
                    partneOperation.setCreated(_c.getString(_c.getColumnIndex("created_at")));
                    partneOperation.setStoreName(_c.getString(_c.getColumnIndex("store_name")));
                    partneOperation.setPointStart(_c.getString(_c.getColumnIndex("point_start")));
                    partneOperation.setStartAddressInit(_c.getString(_c.getColumnIndex("address")));
                    partneOperation.setDistancekm(_c.getString(_c.getColumnIndex("distance_km")));
                    partneOperation.setDistanceM(_c.getString(_c.getColumnIndex("distance_metro")));
                    partneOperation.setState(_c.getString(_c.getColumnIndex("state_operation")));
                    partneOperation.setRouter_id_instructions(_c.getString(_c.getColumnIndex("router_id_instructions")));
                    partneOperation.setDeviceId(_c.getString(_c.getColumnIndex("device_id")));


                    _lista.add(partneOperation);
                } while (_c.moveToNext());
            }
            _c.close();
            return _lista;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (!isTransaction) {
                db.getDb().close();
            }
        }

    }


    public boolean createDataBase() {
        boolean isCheckExist = false;

        db = new DBHelper(context, nameDB);
        if (!db.verificarTabelaExiste(nameTbl)) {
            db.getDb().execSQL(OperationPartnerModel.script_db_tbL_operation_partern);
            isCheckExist = true;
        }

        if (db.getDb().isOpen()) {
            db.getDb().setVersion(Utils.getVersionCode(context));
            db.getDb().close();
        }

        return isCheckExist;
    }

}
