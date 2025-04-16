package br.com.stilldistribuidora.stillrtc.db.business;

import android.content.Context;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;

public class ConfigBusiness extends ProviderBusiness {
    ConfigDataAccess configDataAccess;


    public ConfigBusiness(Context context) {
        configDataAccess = new ConfigDataAccess(context);
    }

    public ConfigBusiness(DBHelper db, boolean isTransaction) {
        configDataAccess = new ConfigDataAccess(db, isTransaction);
    }


    @Override
    public void validateInsert(Object obj) {

    }

    @Override
    public void validateUpdate(Object obj) {

    }

    @Override
    public long insert(Object obj) {
        return configDataAccess.insert(obj);
    }

    @Override
    public long update(Object obj, String where) {
        return 0;
    }

    @Override
    public void delete(String where) {


    }

    @Override
    public Object getById(String where) {
        return configDataAccess.getById(where);
    }

    @Override
    public List<?> getList(String where, String orderby) {

        return configDataAccess.getList(where,orderby);
    }

    public boolean createDataBase() {
        // TODO Auto-generated method stub
        return configDataAccess.createDataBase();
    }

}
