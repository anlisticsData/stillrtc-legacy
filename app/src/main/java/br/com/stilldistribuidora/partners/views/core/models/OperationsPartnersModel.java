package br.com.stilldistribuidora.partners.views.core.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.partners.views.core.contract.partnersOperationContract;
import br.com.stilldistribuidora.partners.views.core.contract.partnersPhotosOperationContract;
import br.com.stilldistribuidora.partners.views.core.databaseContract.PartnersOperationContractReaderDbHelper;
import br.com.stilldistribuidora.partners.views.core.databaseInterface.IModel;
import br.com.stilldistribuidora.partners.views.core.entity.OperationPartnerEntity;
import br.com.stilldistribuidora.partners.views.core.lib.PartenerHelpes;

public class OperationsPartnersModel extends PartnersOperationContractReaderDbHelper implements IModel {
    public OperationsPartnersModel(Context context) {
        super(context);
    }


    @Override
    public Object by(Object object) {
        return null;
    }

    @Override
    public long save(Object object) {

        long insert=0;
        try {
            OperationPartnerEntity operationPartnerEntity = (OperationPartnerEntity) object;
            // Obtém o repositório de dados no modo de gravação
            db =  getWritableDatabase();
            // Crie um novo mapa de valores, onde os nomes das colunas são as chaves
            ContentValues values = new ContentValues();

            values.put( partnersOperationContract.partnersOperationContractEntry.COLUMN_DELIVERI_ID ,operationPartnerEntity.getDelivery_fragment_id());
            values.put( partnersOperationContract.partnersOperationContractEntry.COLUMN_ROUTER_ID ,operationPartnerEntity.getRoute_id());
            values.put( partnersOperationContract.partnersOperationContractEntry.COLUMN_SYNC ,operationPartnerEntity.getSync());
            values.put( partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_AT ,operationPartnerEntity.getCreated_at());
            values.put( partnersOperationContract.partnersOperationContractEntry.COLUMN_LATITUDE ,operationPartnerEntity.getLatitude());
            values.put( partnersOperationContract.partnersOperationContractEntry.COLUMN_LONGITUDE ,operationPartnerEntity.getLongitude());
            values.put( partnersOperationContract.partnersOperationContractEntry.COLUMN_ID_PARTNERS ,operationPartnerEntity.getId_parteners());
            values.put( partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_DEVICE_ID ,operationPartnerEntity.getDeviceId());


            // Insira a nova linha, retornando o valor da chave primária da nova linha
            insert = db.insert( partnersOperationContract.partnersOperationContractEntry.TABLE_NAME, null, values);
        }catch (Exception e){
            System.out.println("#4 "+e.getMessage());
        }
        return insert;



    }

    @Override
    public int delete(Object object) {
        int deletedRows=0;
        try{
            // Define a parte 'onde' da consulta.
            String selection = partnersOperationContract.partnersOperationContractEntry.COLUMN_ID + "=?";
            // Especifique os argumentos na ordem do espaço reservado.
            String[] selectionArgs = { ((String)object)};
            // Emitir instrução SQL.
            deletedRows = db.delete(partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME, selection, selectionArgs);
        }catch (Exception e){}
        return deletedRows;
    }
    @Override
    public int update(Object object) {
        return 0;
    }

    public int operationSyncedOnServer(Object object) {
        int count=0;
        try{
            db = getWritableDatabase();
            OperationPartnerEntity operationPartnerEntity = (OperationPartnerEntity) object;
            // New value for one column
            ContentValues values = new ContentValues();
            values.put(  partnersOperationContract.partnersOperationContractEntry.COLUMN_SYNC ,operationPartnerEntity.getSync());
            values.put(  partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_AT , PartenerHelpes.getDate());

            // Which row to update, based on the title
            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID + "=?";
            String[] selectionArgs = {String.valueOf(operationPartnerEntity.getId()) };
            count = db.update(
                    partnersOperationContract.partnersOperationContractEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }catch (Exception e){
            System.out.println("#4 "+e.getMessage());
        }
        return count;
    }


    public Object operationHasInDatabase(String deviveryId,String parternId) {
        List<OperationPartnerEntity> itemOperations = new ArrayList<>();
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_ID ,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_ID_PARTNERS,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_LATITUDE,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_LONGITUDE,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_DELIVERI_ID,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_ROUTER_ID,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_SYNC,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_AT,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_DEVICE_ID
            };

            // Filtra os resultados WHERE "title" = 'My Title'
            String selection = partnersOperationContract.partnersOperationContractEntry.COLUMN_DELIVERI_ID + " = ?";
            selection+=" and "+ partnersOperationContract.partnersOperationContractEntry.COLUMN_ID_PARTNERS + " = ?";

            String[] selectionArgs = {deviveryId,parternId };
            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_AT+ " DESC";
            Cursor cursor = db.query(
                    partnersOperationContract.partnersOperationContractEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                OperationPartnerEntity operationEntity=new OperationPartnerEntity();
                operationEntity.setId(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_ID)));
                itemOperations.add(operationEntity);
            }
            cursor.close();
        }catch (Exception e){}
        return itemOperations;
    }




    public Object haveOperationsToSync(Object object) {
        List<OperationPartnerEntity> itemOperations = new ArrayList<>();
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_ID ,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_ID_PARTNERS,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_LATITUDE,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_LONGITUDE,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_DELIVERI_ID,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_ROUTER_ID,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_SYNC,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_AT,
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_DEVICE_ID
            };

            // Filtra os resultados WHERE "title" = 'My Title'
            String selection = partnersOperationContract.partnersOperationContractEntry.COLUMN_SYNC + " = ?";
            String[] selectionArgs = { ((String) object) };
            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_AT+ " DESC";
            Cursor cursor = db.query(
                    partnersOperationContract.partnersOperationContractEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                OperationPartnerEntity operationEntity=new OperationPartnerEntity();
                operationEntity.setId(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_ID)));
                operationEntity.setId_parteners(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_ID_PARTNERS)));
                operationEntity.setDelivery_fragment_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_DELIVERI_ID)));
                operationEntity.setRoute_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_ROUTER_ID)));
                operationEntity.setSync(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_SYNC)));
                operationEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_AT)));
                operationEntity.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_LATITUDE)));
                operationEntity.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_LONGITUDE)));
                operationEntity.setDeviceId(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationContract.partnersOperationContractEntry.COLUMN_CREATED_DEVICE_ID)));


                itemOperations.add(operationEntity);
            }
            cursor.close();
        }catch (Exception e){}
        return itemOperations;
    }



}
