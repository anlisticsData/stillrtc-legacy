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
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

public class OperationDataAccess extends ProviderDataAccess {

    private String nameTbl = "tbl_operation";
    private String nameDB = nameTbl.concat(".db");

    private boolean isTransaction = false;

    public OperationDataAccess(Context context) {
        this.context = context;
    }

    public OperationDataAccess(DBHelper db, boolean isTransaction) {
        this.db = db;
        this.isTransaction = isTransaction;
    }

    @Override
    public long insert(Object obj) {
        long _id = 0;
        try {
            if (!isTransaction)
                db = new DBHelper(context, nameDB);

            ContentValues _values = new ContentValues();

            Operation operation = (Operation) obj;

            _values.put(Constants.KEY_DATETIME_START, operation.getDatetimeStart());
            _values.put(Constants.KEY_DATETIME_END, operation.getDatetimeEnd());
            _values.put(Constants.KEY_DELIVERY_FRAGMENT_ID, operation.getDeliveryFragmentId());
            _values.put(Constants.KEY_ROUTE_ID, operation.getRouteId());
            _values.put(Constants.KEY_DISTANCE, operation.getDistance());
            _values.put(Constants.KEY_QNT_PICTURES, operation.getQntPictures());
            _values.put(Constants.KEY_STATUS, operation.getStatus());
            _values.put(Constants.KEY_DEVICE_ID, operation.getDeviceId());
            _values.put(Constants.KEY_SYNC, operation.getSync());
            _values.put(Constants.KEY_CREATED_AT, DateUtils.recuperarDateTimeAtual());

            _id = db.getDb().insert(nameTbl, "", _values);


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

            Operation operation = (Operation) obj;

            _values.put(Constants.KEY_DATETIME_START, operation.getDatetimeStart());
            _values.put(Constants.KEY_DATETIME_END, operation.getDatetimeEnd());
            _values.put(Constants.KEY_DELIVERY_FRAGMENT_ID, operation.getDeliveryFragmentId());
            _values.put(Constants.KEY_ROUTE_ID, operation.getRouteId());
            _values.put(Constants.KEY_DISTANCE, operation.getDistance());
            _values.put(Constants.KEY_QNT_PICTURES, operation.getQntPictures());
            _values.put(Constants.KEY_STATUS, operation.getStatus());
            _values.put(Constants.KEY_DEVICE_ID, operation.getDeviceId());
            _values.put(Constants.KEY_SYNC, operation.getSync());
            _values.put(Constants.KEY_CREATED_AT, DateUtils.recuperarDateTimeAtual());


            _id = db.getDb().update(nameTbl, _values, where, null);

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

            String _query = "DELETE FROM " + nameTbl;

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
            return db.getDb().query(nameTbl, columns, where, null, null, null, orderby);
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

            List<Object> _lista = new ArrayList<>();

            if (_c.moveToFirst()) {
                do {

                    Operation user = new Operation();

                    user.setId(_c.getInt(0));
                    user.setDatetimeStart(_c.getString(1));
                    user.setDatetimeEnd(_c.getString(2));
                    user.setDeliveryFragmentId(_c.getInt(3));
                    user.setRouteId(_c.getInt(4));
                    user.setDistance(_c.getInt(5));
                    user.setQntPictures(_c.getInt(6));
                    user.setStatus(_c.getInt(7));
                    user.setDeviceId(_c.getInt(8));
                    user.setSync(_c.getInt(9));
                    user.setCreatedAt(_c.getString(10));

                    _lista.add(user);

                } while (_c.moveToNext());
            }

            _c.close();

            return _lista;
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

        if (!db.verificarTabelaExiste(nameTbl)) {

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
            Constants.KEY_DATETIME_START,
            Constants.KEY_DATETIME_END,
            Constants.KEY_DELIVERY_FRAGMENT_ID,
            Constants.KEY_ROUTE_ID,
            Constants.KEY_DISTANCE,
            Constants.KEY_QNT_PICTURES,
            Constants.KEY_STATUS,
            Constants.KEY_DEVICE_ID,
            Constants.KEY_SYNC,
            Constants.KEY_CREATED_AT
    };

    private String script_db_tbl = " CREATE TABLE IF NOT EXISTS " + nameTbl + "("
            + " " + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " " + Constants.KEY_DATETIME_START + " DATETIME NULL, "
            + " " + Constants.KEY_DATETIME_END + " DOUBLE NULL, "
            + " " + Constants.KEY_DELIVERY_FRAGMENT_ID + " INT NULL, "
            + " " + Constants.KEY_ROUTE_ID + " INT NULL, "
            + " " + Constants.KEY_DISTANCE + " INT NULL, "
            + " " + Constants.KEY_QNT_PICTURES + " INT NULL, "
            + " " + Constants.KEY_STATUS + " INTEGER DEFAULT 0, "
            + " " + Constants.KEY_DEVICE_ID + " INT NULL, "
            + " " + Constants.KEY_SYNC + " INTEGER DEFAULT 0, "
            + " " + Constants.KEY_CREATED_AT + " DATETIME NULL)";
}
