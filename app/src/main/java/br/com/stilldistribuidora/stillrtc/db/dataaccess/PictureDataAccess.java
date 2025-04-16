package br.com.stilldistribuidora.stillrtc.db.dataaccess;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.models.Picture;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

public class PictureDataAccess extends ProviderDataAccess {

    private String tblName = "tbPicture";
    private String nameDB = tblName.concat(".db");

    private boolean isTransaction = false;

    public PictureDataAccess(Context context) {
        this.context = context;
    }

    public PictureDataAccess(DBHelper db, boolean isTransaction) {
        this.db = db;
        this.isTransaction = isTransaction;
    }

    @Override
    public long insert(Object obj) {
        long _id = 0;
        try {
            if (!isTransaction)
                db = new DBHelper(context, tblName.concat(".db"));

            ContentValues _values = new ContentValues();

            Picture _pic = (Picture) obj;

            _values.put(Constants.KEY_URI, _pic.getUri());
            _values.put(Constants.KEY_LATITUDE, _pic.getLatitude());
            _values.put(Constants.KEY_LONGITUDE, _pic.getLongitude());
            _values.put(Constants.KEY_PIC_ID, _pic.getPicId());
            _values.put(Constants.KEY_TYPE, _pic.getType());
            _values.put(Constants.KEY_DEVICE_ID, _pic.getDeviceId());
            _values.put(Constants.KEY_DELIVERY_ID, _pic.getDeliveryId());
            _values.put(Constants.KEY_SYNC, _pic.getSync());
            _values.put(Constants.KEY_CREATED_AT, DateUtils.recuperarDateTimeAtual());
            _values.put(Constants.KEY_UPDATED_AT, _pic.getUpdatedAt());

            _id = db.getDb().insert(tblName, "", _values);

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
        long _id = 0;
        try {
            if (!isTransaction)
                db = new DBHelper(context, nameDB);

            ContentValues _values = new ContentValues();

            Picture _pic = (Picture) obj;

            _values.put(Constants.KEY_URI, _pic.getUri());
            _values.put(Constants.KEY_LATITUDE, _pic.getLatitude());
            _values.put(Constants.KEY_LONGITUDE, _pic.getLongitude());
            _values.put(Constants.KEY_PIC_ID, _pic.getPicId());
            _values.put(Constants.KEY_TYPE, _pic.getType());
            _values.put(Constants.KEY_DEVICE_ID, _pic.getDeviceId());
            _values.put(Constants.KEY_DELIVERY_ID, _pic.getDeliveryId());
            _values.put(Constants.KEY_SYNC, _pic.getSync());
            _values.put(Constants.KEY_CREATED_AT, _pic.getCreatedAt());
            _values.put(Constants.KEY_UPDATED_AT, DateUtils.recuperarDateTimeAtual());

            _id = db.getDb().update(tblName, _values, where, null);

            return _id;
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        } finally {
            if (!isTransaction)
                db.getDb().close();
        }
    }

    @Override
    public void delete(String where) {
        try {
            if (!isTransaction)
                db = new DBHelper(context, nameDB);

            String _query = "DELETE FROM " + tblName;

            if (where.length() > 0)
                _query += " WHERE " + where;

            db.getDb().execSQL(_query);
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        } finally {
            if (!isTransaction)
                db.getDb().close();
        }
    }

    @Override
    public Cursor getCursor(String where, String orderby) {
        try {
            return db.getDb().query(tblName, columns, where, null, null, null, orderby);
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());

        }
    }

    @Override
    public Object getById(String where) {
        Object _obj;
        try {
            _obj = getList(where, "").get(0);
            return _obj;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<?> getList(String where, String orderby) {
        try {
            if (!isTransaction)
                db = new DBHelper(context, nameDB);

            Cursor _c = getCursor(where, orderby);

            List<Object> _list = new ArrayList<>();

            if (_c.moveToFirst()) {
                do {

                    Picture _pic = new Picture();

                    _pic.setId(_c.getInt(0));
                    _pic.setUri(_c.getString(1));
                    _pic.setLatitude(_c.getDouble(2));
                    _pic.setLongitude(_c.getDouble(3));
                    _pic.setPicUid(_c.getInt(4));
                    _pic.setType(_c.getInt(5));
                    _pic.setDeviceId(_c.getInt(6));
                    _pic.setDeliveryId(_c.getInt(7));
                    _pic.setSync(_c.getInt(8));
                    _pic.setCreatedAt(_c.getString(9));
                    _pic.setUpdatedAt(_c.getString(10));

                    _list.add(_pic);

                } while (_c.moveToNext());
            }

            _c.close();

            return _list;
        } catch (Exception e) {

            throw new RuntimeException(e.getMessage());
        } finally {
            if (!isTransaction)
                db.getDb().close();
        }
    }

    public boolean createDataBase() {

        boolean _retorno = false;

        db = new DBHelper(context, nameDB);

        if (!db.verificarTabelaExiste(tblName)) {

            db.getDb().execSQL(script_db_tbl);

            _retorno = true;
        }

        if (db.getDb().isOpen()) {
            db.getDb().setVersion(Utils.getVersionCode(context));
            db.getDb().close();
        }

        return _retorno;
    }

    private String[] columns = new String[]{
            Constants.ID,
            Constants.KEY_URI,
            Constants.KEY_LATITUDE,
            Constants.KEY_LONGITUDE,
            Constants.KEY_PIC_ID,
            Constants.KEY_TYPE,
            Constants.KEY_DEVICE_ID,
            Constants.KEY_DELIVERY_ID,
            Constants.KEY_SYNC,
            Constants.KEY_CREATED_AT,
            Constants.KEY_UPDATED_AT
    };

    private String script_db_tbl = " CREATE TABLE IF NOT EXISTS " + tblName + "("
            + " " + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " " + Constants.KEY_URI + " TEXT, "
            + " " + Constants.KEY_LATITUDE + " DOUBLE NULL, "
            + " " + Constants.KEY_LONGITUDE + " DOUBLE NULL, "
            + " " + Constants.KEY_PIC_ID + " INTEGER NULL, "
            + " " + Constants.KEY_TYPE + " INTEGER DEFAULT 0, "
            + " " + Constants.KEY_DEVICE_ID + " INTEGER NULL, "
            + " " + Constants.KEY_DELIVERY_ID + " INTEGER NULL, "
            + " " + Constants.KEY_SYNC + " INTEGER DEFAULT 0, "
            + " " + Constants.KEY_CREATED_AT + " DATETIME NULL, "
            + " " + Constants.KEY_UPDATED_AT + " DATETIME NULL)";
}
