package br.com.stilldistribuidora.stillrtc.db.business;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import android.content.Context;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.OperationDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;

public class OperationBusiness extends ProviderBusiness {

    OperationDataAccess deviceDa;

    public OperationBusiness(Context context) {
        deviceDa = new OperationDataAccess(context);
    }

    public OperationBusiness(DBHelper db, boolean isTransaction) {
        deviceDa = new OperationDataAccess(db, isTransaction);
    }

    public void validateInsert(Object obj) {
        Operation _user = (Operation) obj;
        if (_user.getId() == 0) {
            throw new RuntimeException("DeviceId nao informado");
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