package br.com.stilldistribuidora.stillrtc.db.business;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import android.content.Context;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ContactDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Contact;

public class ContactBusiness extends ProviderBusiness {

    ContactDataAccess deviceDa;

    public ContactBusiness(Context context) {
        deviceDa = new ContactDataAccess(context);
    }

    public ContactBusiness(DBHelper db, boolean isTransaction) {
        deviceDa = new ContactDataAccess(db, isTransaction);
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
        deviceDa.insert(obj);

        return 0;

    }

    @Override
    public long update(Object obj, String where) {
        // TODO Auto-generated method stub
        return deviceDa.update(obj, where);
    }

    @Override
    public void delete(String where) {
        // TODO Auto-generated method stub
        deviceDa.delete(where);
    }

    @Override
    public Object getById(String where) {
        // TODO Auto-generated method stub
        return deviceDa.getById(where);
    }

    @Override
    public List<?> getList(String where, String orderby) {
        // TODO Auto-generated method stub
        return deviceDa.getList(where, orderby);
    }

    @Override
    public void validateUpdate(Object obj) {
        // TODO Auto-generated method stub

    }

    public boolean createDataBase() {
        // TODO Auto-generated method stub
        return deviceDa.createDataBase();
    }
}