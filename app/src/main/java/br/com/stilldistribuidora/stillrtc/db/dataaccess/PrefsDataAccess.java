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
import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

public class PrefsDataAccess extends ProviderDataAccess {

    private String nameTbl = "tbl_prefs_app";
    private String nameDB = nameTbl.concat(".db");

    private boolean isTransaction = false;
    //ext context;

    public PrefsDataAccess(Context context) {
        this.context = context;
    }

    public PrefsDataAccess(DBHelper db, boolean isTransaction) {
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

            Prefs user = (Prefs) obj;

            _values.put(Constants.KEY_TIME_LOOP_PICTURES, user.getTimeLoopPictures());
            _values.put(Constants.KEY_TIME_LOOP_POINTS, user.getTimeLoopPoints());
            _values.put(Constants.KEY_CREATOR_ID, user.getCreator_id());
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

    @SuppressWarnings("static-access")
    @Override
    public long update(Object obj, String where) {
        long _id = 0;
        try {
            if (!isTransaction)
                db = new DBHelper(context, nameDB);

            ContentValues _values = new ContentValues();

            Prefs user = (Prefs) obj;

            _values.put(Constants.KEY_TIME_LOOP_PICTURES, user.getTimeLoopPictures());
            _values.put(Constants.KEY_TIME_LOOP_POINTS, user.getTimeLoopPoints());
            _values.put(Constants.KEY_CREATOR_ID, user.getCreator_id());
            _values.put(Constants.KEY_CREATED_AT, user.getCreated_at());

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
        Object _obj = null;
        try {
            _obj = getList(where, "").get(0);
            return _obj;
        } catch (Exception e) {
            return _obj;
        }
    }

    @Override
    public List<?> getList(String where, String orderby) {
        try {
            if (!isTransaction)
                db = new DBHelper(context, nameDB);

           Cursor _c = getCursor(where, orderby);

            List<Object> _lista = new ArrayList<Object>();

            if (_c.moveToFirst()) {
                do {

                    Prefs device = new Prefs();

                    device.setId(_c.getInt(0));
                    device.setTime_loop_pictures(_c.getInt(1));
                    device.setTime_loop_points(_c.getInt(2));
                    device.setCreator_id(_c.getInt(3));
                    device.setCreated_at(_c.getString(4));

                    _lista.add(device);

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
            Constants.KEY_TIME_LOOP_PICTURES,
            Constants.KEY_TIME_LOOP_POINTS,
            Constants.KEY_CREATOR_ID,
            Constants.KEY_CREATED_AT
    };

    private String script_db_tbl = " CREATE TABLE IF NOT EXISTS " + nameTbl + "("
            + " " + Constants.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + " " + Constants.KEY_TIME_LOOP_PICTURES + " INT NULL, "
            + " " + Constants.KEY_TIME_LOOP_POINTS + " INT NULL, "
            + " " + Constants.KEY_CREATOR_ID + " INT NULL, "
            + " " + Constants.KEY_CREATED_AT + " DATETIME NULL)";

}
