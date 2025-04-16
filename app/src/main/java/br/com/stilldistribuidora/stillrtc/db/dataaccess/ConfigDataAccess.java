package br.com.stilldistribuidora.stillrtc.db.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

public class ConfigDataAccess extends ProviderDataAccess {
    private final String nameDB = Config.class.getSimpleName() + ".db";
    private final String nameTbl = Config.class.getSimpleName();

    private boolean isTransaction = false;


    public ConfigDataAccess(Context context) {
        this.context = context;
    }

    public ConfigDataAccess(DBHelper db, boolean isTransaction) {
        this.db = db;
        this.isTransaction = isTransaction;
    }


    @Override
    public long insert(Object obj) {
        long _id = 0;
        try {
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }
            ContentValues _values = new ContentValues();
            Config config = (Config) obj;
            _values.put(Constants.CONFIG_DESCRICAO, config.getDescricao());
            _values.put(Constants.CONFIG_OPCAO, config.getDataJson());
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
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }
            ContentValues _values = new ContentValues();
            Config config = (Config) obj;
            _values.put(Constants.CONFIG_OPCAO, config.getDataJson());
            _id = db.getDb().update(nameTbl,_values,Constants.CONFIG_DESCRICAO+"=?",new String[]{config.getDescricao()});
            return _id;
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (!isTransaction)
                db.getDb().close();
        }




    }

    @Override
    public void delete(String where) {

    }

    @Override
    public Cursor getCursor(String orderby, String where) {
        try {
            return db.getDb().query(nameTbl, Config.columns, where, null, null, null, orderby);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Object getById(String where) {
        Object _obj = null;
        try {
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }
            Cursor _c = db.getDb().query(nameTbl, Config.columns, where, null, null, null, null);
            List<Object> _lista = new ArrayList<Object>();
            if (_c.moveToFirst()) {
                do {
                    Config configs = new Config();
                    configs.setId(_c.getInt(0));
                    configs.setDescricao(_c.getString(1));
                    configs.setDataJson(_c.getString(2));
                    _lista.add(configs);
                } while (_c.moveToNext());
            }
            _c.close();
            _obj=_lista.get(0);
            return _obj;
        } catch (Exception e) {
            return _obj;
        }
    }

    @Override
    public List<?> getList(String where, String orderby) {
        try {
            if (!isTransaction) {
                db = new DBHelper(context, nameDB);
            }
            Cursor _c = getCursor(where, orderby);
            List<Object> _lista = new ArrayList<Object>();
            if (_c.moveToFirst()) {
                do {
                    Config configs = new Config();
                    configs.setId(_c.getInt(0));
                    configs.setDescricao(_c.getString(1));
                    configs.setDataJson(_c.getString(2));
                    _lista.add(configs);
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
        boolean _retorno = false;
        db = new DBHelper(context, nameDB);
        if (!db.verificarTabelaExiste(nameTbl)) {
            db.getDb().execSQL(Config.script_db_tblConfig);
            _retorno = true;
        }
        if (db.getDb().isOpen()) {
            db.getDb().setVersion(Utils.getVersionCode(context));
            db.getDb().close();
        }
        return _retorno;
    }


}
