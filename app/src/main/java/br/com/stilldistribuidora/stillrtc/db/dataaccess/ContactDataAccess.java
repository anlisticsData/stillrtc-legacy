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
import br.com.stilldistribuidora.stillrtc.db.models.Contact;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

public class ContactDataAccess extends ProviderDataAccess {

    private String nameDB = Contact.class.getSimpleName()+".db";
    private String nameTbl = Contact.class.getSimpleName();

    private boolean isTransaction = false;
    //ext context;

    public ContactDataAccess(Context context) {
        this.context = context;
    }

    public ContactDataAccess(DBHelper db, boolean isTransaction) {
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

            Contact user = (Contact) obj;

            _values.put(Constants.CONTACT_ID, user.getContactID());
            _values.put(Constants.NAME, user.getName());
            _values.put(Constants.NUMBER, user.getNumber());
            _values.put(Constants.EMAIL, user.getEmail());
            _values.put(Constants.KEY_CREATED_AT, DateUtils.recuperarDateTimeAtual());
            _values.put(Constants.IMAGE, user.getImage());

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

            Contact user = (Contact) obj;

            _values.put(Constants.CONTACT_ID, user.getContactID());
            _values.put(Constants.NAME, user.getName());
            _values.put(Constants.NUMBER, user.getNumber());
            _values.put(Constants.EMAIL, user.getEmail());
            _values.put(Constants.KEY_CREATED_AT, user.getCreated_at());
            _values.put(Constants.IMAGE, user.getImage());

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
            return db.getDb().query(nameTbl, Contact.columns, where, null, null, null, orderby);
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

                    Contact device = new Contact();

                    device.setId(_c.getInt(0));
                    device.setContactID(_c.getInt(1));
                    device.setName(_c.getString(2));
                    device.setNumber(_c.getString(3));
                    device.setEmail(_c.getString(4));
                    device.setCreated_at(_c.getString(5));
                    device.setImage(_c.getString(6));

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

            db.getDb().execSQL(Contact.script_db_tblContact);

            _retorno = true;
        }

        if (db.getDb().isOpen()) {
            db.getDb().setVersion(Utils.getVersionCode(context));
            db.getDb().close();
        }

        return _retorno;
    }

}
