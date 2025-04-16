package br.com.stilldistribuidora.stillrtc.db.business;

import android.content.Context;

import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.DBHelper;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ContactDataAccess;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.OperationPartner;

public class OperationPartnerBusines extends ProviderBusiness{

    OperationPartner operationPartner;

    public OperationPartnerBusines(Context context) {
        operationPartner = new OperationPartner(context);
        operationPartner.createDataBase();
    }

    public OperationPartnerBusines(DBHelper db, boolean isTransaction) {
        operationPartner = new OperationPartner(db, isTransaction);
        operationPartner.createDataBase();

    }



    @Override
    public void validateInsert(Object obj) {

    }

    @Override
    public void validateUpdate(Object obj) {

    }

    @Override
    public long insert(Object obj) {
        return operationPartner.insert(obj);
    }

    @Override
    public long update(Object obj, String where) {
        return operationPartner.update(obj,where);
    }

    @Override
    public void delete(String where) {
        operationPartner.delete(where);
    }

    @Override
    public Object getById(String where) {
        return operationPartner.getById(where);
    }

    @Override
    public List<?> getList(String where, String orderby) {
        return operationPartner.getList(where,orderby);
    }


    public Object hasOperationPartnersInDabe(String delivery,String partner){
        return operationPartner.hasOperationPartnersInDabe(delivery,partner);
    }

    public long updateState(String state,  String operationId,String idpartners,String deviceId) {
        return operationPartner.updateState(state,operationId,idpartners,deviceId);
    }



    public long updateState(String state,  String operationId,String deviceId) {
        return operationPartner.updateState(state,operationId,deviceId);
    }

    public long updateState(String state, String operationId) {
        return operationPartner.updateState(state,operationId);
    }




}
