package br.com.stilldistribuidora.stillrtc.db.business;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import android.content.Context;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.PrefsDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Contact;

public class PrefsBusiness extends ProviderBusiness {

    private PrefsDataAccess item;

    public PrefsBusiness(Context context) {
        item = new PrefsDataAccess(context);
    }

    public PrefsBusiness(DBHelper db, boolean isTransaction) {
        item = new PrefsDataAccess(db, isTransaction);
    }

    public void validateInsert(Object obj) {
        Contact _user = (Contact) obj;
        if (_user.getId() == 0) {
            throw new RuntimeException("DeviceId nao informado");
        }

        if (_user.getName().length()==0) {
            throw new RuntimeException("Produto nao informado");
        }
    }

    @Override
    public long insert(Object obj) {
        // TODO Auto-generated method stub
        item.insert(obj);

        return 0;

    }

    @Override
    public long update(Object obj, String where) {
        // TODO Auto-generated method stub
        return item.update(obj, where);
    }

    @Override
    public void delete(String where) {
        // TODO Auto-generated method stub
        item.delete(where);
    }

    @Override
    public Object getById(String where) {
        // TODO Auto-generated method stub
        return item.getById(where);
    }

    @Override
    public List<?> getList(String where, String orderby) {
        // TODO Auto-generated method stub
        return item.getList(where, orderby);
    }

    @Override
    public void validateUpdate(Object obj) {
        // TODO Auto-generated method stub

    }

    public boolean createDataBase() {
        // TODO Auto-generated method stub
        return item.createDataBase();
    }
}