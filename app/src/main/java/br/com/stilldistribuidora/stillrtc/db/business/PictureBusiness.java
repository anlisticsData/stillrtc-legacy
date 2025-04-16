package br.com.stilldistribuidora.stillrtc.db.business;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.PictureDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Picture;

public class PictureBusiness extends ProviderBusiness {

    private static final String TAG = PictureBusiness.class.getSimpleName();
    private PictureDataAccess pictureDataAccess;

    public PictureBusiness(Context context) {
        pictureDataAccess = new PictureDataAccess(context);
    }

    public PictureBusiness(DBHelper db, boolean isTransaction) {
        pictureDataAccess = new PictureDataAccess(db, isTransaction);
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
        // TODO Auto-generated method stub
        Picture picture = (Picture) obj;
        pictureDataAccess.insert(obj);

        Log.i(TAG, "--------------Insert Picture ----------------"
                + "\nLat:" + picture.getLatitude()
                + "\nLng:" + picture.getLongitude()
                + "\ntype:" + picture.getType()
                + "\nSync:" + picture.getSync()
                + "\nDeviceId:" + picture.getDeviceId()
                + "\nDeliveryId:" + picture.getDeliveryId()
                + "\nCreatedAt:" + picture.getCreatedAt()
                + "\nUpdatedAt:" + picture.getUpdatedAt());


        return 0;

    }

    @Override
    public long update(Object obj, String where) {
        // TODO Auto-generated method stub
        return pictureDataAccess.update(obj, where);
    }

    @Override
    public void delete(String where) {
        // TODO Auto-generated method stub
        pictureDataAccess.delete(where);
    }

    @Override
    public Object getById(String where) {
        // TODO Auto-generated method stub
        return pictureDataAccess.getById(where);
    }

    @Override
    public List<?> getList(String where, String orderby) {
        // TODO Auto-generated method stub
        return pictureDataAccess.getList(where, orderby);
    }

    @Override
    public void validateUpdate(Object obj) {
        // TODO Auto-generated method stub

    }

    public boolean createDataBase() {
        // TODO Auto-generated method stub
        return pictureDataAccess.createDataBase();
    }
}