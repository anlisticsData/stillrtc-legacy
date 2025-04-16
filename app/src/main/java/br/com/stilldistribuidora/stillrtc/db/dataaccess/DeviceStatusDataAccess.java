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
import br.com.stilldistribuidora.stillrtc.db.models.DeviceStatus;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

public class DeviceStatusDataAccess extends ProviderDataAccess {

    private String tblName = "tbDeviceStatus";
    private String nameDB = tblName.concat(".db");

    private boolean isTransaction = false;

    public DeviceStatusDataAccess(Context context) {
        this.context = context;
    }

    public DeviceStatusDataAccess(DBHelper db, boolean isTransaction) {
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

            DeviceStatus _ds = (DeviceStatus) obj;

            _values.put(Constants.KEY_LATITUDE, _ds.getLatitude());
            _values.put(Constants.KEY_LONGITUDE, _ds.getLongitude());
            _values.put(Constants.KEY_DELIVERY_FRAGMENT_ID, _ds.getDeviceFragmentId());
            _values.put(Constants.KEY_DEVICE_IDENTIFIER, _ds.getDeviceIdentifier());
            _values.put(Constants.KEY_BATTERY, _ds.getBatery());
            _values.put(Constants.KEY_SYNC, _ds.getSync());
            _values.put(Constants.KEY_IP, _ds.getIp());
            _values.put(Constants.KEY_CREATED_AT, DateUtils.recuperarDateTimeAtual());

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

            DeviceStatus _ds = (DeviceStatus) obj;

            _values.put(Constants.KEY_LATITUDE, _ds.getLatitude());
            _values.put(Constants.KEY_LONGITUDE, _ds.getLongitude());
            _values.put(Constants.KEY_DELIVERY_FRAGMENT_ID, _ds.getDeviceFragmentId());
            _values.put(Constants.KEY_DEVICE_IDENTIFIER, _ds.getDeviceIdentifier());
            _values.put(Constants.KEY_BATTERY, _ds.getBatery());
            _values.put(Constants.KEY_SYNC, _ds.getSync());
            _values.put(Constants.KEY_IP, _ds.getIp());
            _values.put(Constants.KEY_CREATED_AT, DateUtils.recuperarDateTimeAtual());

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

                    DeviceStatus _pic = new DeviceStatus();

                    _pic.setId(_c.getInt(0));
                    _pic.setLatitude(_c.getDouble(1));
                    _pic.setLongitude(_c.getDouble(2));
                    _pic.setBatery(_c.getInt(3));
                    _pic.setDeviceIdentifier(_c.getString(4));
                    _pic.setDeviceFragmentId(_c.getInt(5));
                    _pic.setSync(_c.getInt(6));
                    _pic.setIp(_c.getString(7));
                    _pic.setCreatedAt(_c.getString(8));

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

        boolean _return = false;

        db = new DBHelper(context, nameDB);

        if (!db.verificarTabelaExiste(tblName)) {

            db.getDb().execSQL(script_db_tbl);

            _return = true;
        }

        if (db.getDb().isOpen()) {
            db.getDb().setVersion(Utils.getVersionCode(context));
            db.getDb().close();
        }

        return _return;
    }

    private String[] columns = new String[]{
            Constants.ID,
            Constants.KEY_LATITUDE,
            Constants.KEY_LONGITUDE,
            Constants.KEY_BATTERY,
            Constants.KEY_DEVICE_IDENTIFIER,
            Constants.KEY_DELIVERY_FRAGMENT_ID,
            Constants.KEY_SYNC,
            Constants.KEY_IP,
            Constants.KEY_CREATED_AT
    };

    private String script_db_tbl = " CREATE TABLE IF NOT EXISTS " + tblName + "("
            + " " + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " " + Constants.KEY_LATITUDE + " DOUBLE NULL, "
            + " " + Constants.KEY_LONGITUDE + " DOUBLE NULL, "
            + " " + Constants.KEY_BATTERY + " INTEGER NULL, "
            + " " + Constants.KEY_DEVICE_IDENTIFIER + " TEXT NULL, "
            + " " + Constants.KEY_DELIVERY_FRAGMENT_ID + " INTEGER NULL, "
            + " " + Constants.KEY_SYNC + " INTEGER DEFAULT 0, "
            + " " + Constants.KEY_IP + " TEXT NULL, "
            + " " + Constants.KEY_CREATED_AT + " DATETIME NULL)";
}
