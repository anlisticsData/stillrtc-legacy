package br.com.stilldistribuidora.partners.views.core.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.partners.views.core.contract.partnersOperationControlStateContract;
import br.com.stilldistribuidora.partners.views.core.databaseContract.PartnersOperationControlStateContractReaderDbHelper;
import br.com.stilldistribuidora.partners.views.core.databaseInterface.IModel;
import br.com.stilldistribuidora.partners.views.core.entity.PartnersOperationControlStateEntity;

public class OperationControlStateModel extends  PartnersOperationControlStateContractReaderDbHelper implements IModel {


    public OperationControlStateModel(Context context) {
        super(context);
    }

    @Override
    public Object by(Object object) {
        List<PartnersOperationControlStateEntity> items = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ROUTER_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_CREATED_AT
            };
            String selection =  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS + " = ? and  ";
            selection +=  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID + " = ?  ";

            String[] selectionArgs =( String[])object;
            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_CREATED_AT + " DESC";
            Cursor cursor = db.query(
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while (cursor.moveToNext()) {
                PartnersOperationControlStateEntity ControlStateEntity = new PartnersOperationControlStateEntity();
                ControlStateEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ID)));

                ControlStateEntity.setId_parteners(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS)));
                ControlStateEntity.setDelivery_fragment_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID)));
                ControlStateEntity.setRoute_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ROUTER_ID)));
                ControlStateEntity.setTime_current(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT)));
                ControlStateEntity.setDelivery_state(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START)));
                ControlStateEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_CREATED_AT)));
                items.add(ControlStateEntity);
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println("#4   "+e.getMessage());
        }
        return items;


    }



    public Object operationStateIsAtivo(Object object) {
        List<PartnersOperationControlStateEntity> items = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ROUTER_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_CREATED_AT
            };
            String selection =  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS + "=? and ";
            selection +=  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID + "=? and ";
            selection +=  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START + "=?";


            String[] selectionArgs =( String[])object;
            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_CREATED_AT + " DESC";
            Cursor cursor = db.query(
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while (cursor.moveToNext()) {
                PartnersOperationControlStateEntity ControlStateEntity = new PartnersOperationControlStateEntity();
                ControlStateEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ID)));

                ControlStateEntity.setId_parteners(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS)));
                ControlStateEntity.setDelivery_fragment_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID)));
                ControlStateEntity.setRoute_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ROUTER_ID)));
                ControlStateEntity.setTime_current(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT)));
                ControlStateEntity.setDelivery_state(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START)));
                ControlStateEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_CREATED_AT)));
                items.add(ControlStateEntity);
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println("#4   "+e.getMessage());
        }
        return items;


    }





    public Object operationState(Object object) {
        List<PartnersOperationControlStateEntity> items = new ArrayList<>();
        try {
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ROUTER_ID,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START,
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_CREATED_AT
            };
            String selection =  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS + "=? and ";
            selection +=  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID + "!=? and ";
            selection +=  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START + "=?";


            String[] selectionArgs =( String[])object;
            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_CREATED_AT + " DESC";
            Cursor cursor = db.query(
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while (cursor.moveToNext()) {
                PartnersOperationControlStateEntity ControlStateEntity = new PartnersOperationControlStateEntity();
                ControlStateEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ID)));

                ControlStateEntity.setId_parteners(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS)));
                ControlStateEntity.setDelivery_fragment_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID)));
                ControlStateEntity.setRoute_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_ROUTER_ID)));
                ControlStateEntity.setTime_current(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT)));
                ControlStateEntity.setDelivery_state(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START)));
                ControlStateEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersOperationControlStateContract.
                        partnersOperationControlStateContractEntry.COLUMN_CREATED_AT)));
                items.add(ControlStateEntity);
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println("#4   "+e.getMessage());
        }
        return items;


    }



    @Override
    public long save(Object object) {
        long insert = 0;
        try {
            PartnersOperationControlStateEntity controlStateModel = (PartnersOperationControlStateEntity) object;
            // Obtém o repositório de dados no modo de gravação
            db = getWritableDatabase();
            // Crie um novo mapa de valores, onde os nomes das colunas são as chaves
            ContentValues values = new ContentValues();
            values.put(partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS, controlStateModel.getId_parteners());
            values.put(partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID, controlStateModel.getDelivery_fragment_id());
            values.put(partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ROUTER_ID, controlStateModel.getRoute_id());
            values.put(partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT, controlStateModel.getTime_current());
            values.put(partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START, controlStateModel.getDelivery_state());
            values.put(partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_CREATED_AT, controlStateModel.getCreated_at());
            // Insira a nova linha, retornando o valor da chave primária da nova linha
            insert = db.insert(partnersOperationControlStateContract.partnersOperationControlStateContractEntry.TABLE_NAME, null, values);
        } catch (Exception e) {
        }


        return insert;
    }


    public  int updateTimeCurrent(Object object){
        int count=0;
        try{
            db = getWritableDatabase();
            PartnersOperationControlStateEntity controlStateEntity = (PartnersOperationControlStateEntity) object;
            // New value for one column
            String title = "MyNewTitle";
            ContentValues values = new ContentValues();

            values.put(  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT,controlStateEntity.getTime_current());



            // Which row to update, based on the title
            String selection = partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID + "=?";
            String[] selectionArgs = {String.valueOf(controlStateEntity.getId()) };

            count = db.update(
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);


        }catch (Exception e){

            System.out.println("#4 "+e.getMessage());
        }

        return count;
    }


    public  int updateCloseOperation(Object object){
        int count=0;
        try{
            db = getWritableDatabase();
            PartnersOperationControlStateEntity controlStateEntity = (PartnersOperationControlStateEntity) object;
            // New value for one column
            String title = "MyNewTitle";
            ContentValues values = new ContentValues();
            values.put(  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT,controlStateEntity.getTime_current());
            // Which row to update, based on the title
            String selection = partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID + "=?";
            String[] selectionArgs = {String.valueOf(controlStateEntity.getId()) };

            count = db.update(
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);


        }catch (Exception e){

            System.out.println("#4 "+e.getMessage());
        }

        return count;
    }



    @Override
    public int delete(Object object) {
        return 0;
    }

    @Override
    public int update(Object object) {
        int count=0;
        try{
            db = getWritableDatabase();
            PartnersOperationControlStateEntity controlStateEntity = (PartnersOperationControlStateEntity) object;
            // New value for one column

            ContentValues values = new ContentValues();

            values.put(  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START,controlStateEntity.getDelivery_state());
            values.put(  partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_CREATED_AT,controlStateEntity.getTime_current());


            // Which row to update, based on the title
            String selection = partnersOperationControlStateContract.partnersOperationControlStateContractEntry.COLUMN_ID + "=?";
            String[] selectionArgs = {String.valueOf(controlStateEntity.getId()) };

            count = db.update(
                    partnersOperationControlStateContract.partnersOperationControlStateContractEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);


        }catch (Exception e){

            System.out.println("#4 "+e.getMessage());
        }

        return count;


    }
}