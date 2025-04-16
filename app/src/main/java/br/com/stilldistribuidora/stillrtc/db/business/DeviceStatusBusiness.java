package br.com.stilldistribuidora.stillrtc.db.business;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.DeviceStatusDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.DeviceStatus;
import br.com.stilldistribuidora.stillrtc.db.models.Picture;

public class DeviceStatusBusiness extends ProviderBusiness {

    private static final String TAG = DeviceStatusBusiness.class.getSimpleName();
    private DeviceStatusDataAccess pictureDataAccess;

    public DeviceStatusBusiness(Context context) {
        pictureDataAccess = new DeviceStatusDataAccess(context);
    }

    public DeviceStatusBusiness(DBHelper db, boolean isTransaction) {
        pictureDataAccess = new DeviceStatusDataAccess(db, isTransaction);
    }

    public void validateInsert(Object obj) {
        Picture _pic = (Picture) obj;
        if (_pic.getId() == 0) {
            throw new RuntimeException("DeviceId nao informado");
        }

        if (_pic.getUri().length() == 0) {
            throw new RuntimeException("Produto nao informado");
        }
    }

    @Override
    public long insert(Object obj) {
        DeviceStatus _ds = (DeviceStatus) obj;
        pictureDataAccess.insert(obj);

        Log.i(TAG, "[Register Device Status OFF]"
                + "(lat: " + _ds.getLatitude()
                + "; lng: " + _ds.getLongitude()
                + "; battery: " + _ds.getBatery()
                + "; sync:" + _ds.getSync()
                + "; created_at:" + _ds.getCreatedAt()+")");


        return 0;

    }

    @Override
    public long update(Object obj, String where) {
        return pictureDataAccess.update(obj, where);
    }

    @Override
    public void delete(String where) {
        pictureDataAccess.delete(where);
    }

    @Override
    public Object getById(String where) {
        return pictureDataAccess.getById(where);
    }

    @Override
    public List<?> getList(String where, String orderby) {
        return pictureDataAccess.getList(where, orderby);
    }

    @Override
    public void validateUpdate(Object obj) {
    }

    public boolean createDataBase() {
        return pictureDataAccess.createDataBase();
    }
}