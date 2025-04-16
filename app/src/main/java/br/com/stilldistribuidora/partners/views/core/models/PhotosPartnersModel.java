package br.com.stilldistribuidora.partners.views.core.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.partners.views.core.contract.partnersPhotosOperationContract;
import br.com.stilldistribuidora.partners.views.core.databaseInterface.IModel;
import br.com.stilldistribuidora.partners.views.core.databaseContract.PartnersPhotoOperationContractReaderDbHelper;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.lib.PartenerHelpes;

public class PhotosPartnersModel extends PartnersPhotoOperationContractReaderDbHelper implements IModel {
    public PhotosPartnersModel(Context context) {
        super(context);

    }

    public    List<PhotoEntity> photoOfTheOperationsThatDidNotGoUpLimit(String limit) {
        List<PhotoEntity> items = new ArrayList<>();
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID ,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID
            };
            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_SYNC + "=0";
            selection+= " or "+partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_SYNC + " is null";


            String[] selectionArgs =null;
            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT+ " DESC";
            Cursor cursor = db.query(
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder,               // The sort order
                    limit
            );

            while(cursor.moveToNext()) {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID)));
                photoEntity.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE)));
                photoEntity.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE)));
                photoEntity.setDelivery_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY)).trim());
                photoEntity.setUri(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI)));
                photoEntity.setPic_uid(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID)));
                photoEntity.setType(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE)));
                photoEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT)));
                photoEntity.setUpdate_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT)));
                photoEntity.setPartners_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS)));
                photoEntity.setPath_serve_url(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL)));
                photoEntity.setDeviceId(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID)));



                items.add(photoEntity);
            }
            cursor.close();
        }catch (Exception e){
            System.out.println("#4 "+e.getMessage());
        }
        return items;
    }




    public    List<PhotoEntity> photoOfTheOperationsThatDidNotGoUp() {
        List<PhotoEntity> items = new ArrayList<>();
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID ,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID
            };
            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_SYNC + " = 0 ";


            String[] selectionArgs =null;
            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT+ " DESC";
            Cursor cursor = db.query(
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID)));
                photoEntity.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE)));
                photoEntity.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE)));
                photoEntity.setDelivery_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY)));
                photoEntity.setUri(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI)));
                photoEntity.setPic_uid(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID)));
                photoEntity.setType(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE)));
                photoEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT)));
                photoEntity.setUpdate_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT)));
                photoEntity.setPartners_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS)));
                photoEntity.setPath_serve_url(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL)));
                photoEntity.setDeviceId(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID)));



                items.add(photoEntity);
            }
            cursor.close();
        }catch (Exception e){
            System.out.println("#4 "+e.getMessage());
        }
        return items;
    }



    @Override
    public Object by(Object object) {
        List itemIds = new ArrayList<>();
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID ,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID
            };


            // Filtra os resultados WHERE "title" = 'My Title'

            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID + " = ?";
            String[] selectionArgs = { ((String) object) };

            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT+ " DESC";

            Cursor cursor = db.query(
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID));
                itemIds.add(itemId);
            }
            cursor.close();
        }catch (Exception e){}
        return itemIds;
    }



    public  Object photosAllByUser(Object object) {
        List<PhotoEntity> items = new ArrayList<>();
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID ,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID
            };




            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS + " = ? and ";
            selection+= partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY + " = ?";

            String[] selectionArgs =((String[]) object);

            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT+ " DESC";

            Cursor cursor = db.query(
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID)));
                photoEntity.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE)));
                photoEntity.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE)));
                photoEntity.setDelivery_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY)));
                photoEntity.setUri(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI)));
                photoEntity.setPic_uid(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID)));
                photoEntity.setType(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE)));
                photoEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT)));
                photoEntity.setUpdate_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT)));
                photoEntity.setPartners_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS)));
                photoEntity.setPath_serve_url(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL)));
                photoEntity.setDeviceId(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID)));



                items.add(photoEntity);
            }
            cursor.close();
        }catch (Exception e){
            System.out.println("#4 "+e.getMessage());
        }
        return items;
    }

    public  List<PhotoEntity>   photosAllByOperation(String deliveryId) {
        List<PhotoEntity> items = new ArrayList<>();
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID ,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID
            };
            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY + " = ?";
            String[] selectionArgs = { deliveryId };
            String sortOrder =
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT+ " DESC";
            Cursor cursor = db.query(
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID)));
                photoEntity.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE)));
                photoEntity.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE)));
                photoEntity.setDelivery_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY)));
                photoEntity.setUri(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI)));
                photoEntity.setPic_uid(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID)));
                photoEntity.setType(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE)));
                photoEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT)));
                photoEntity.setUpdate_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT)));
                photoEntity.setPartners_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS)));
                photoEntity.setPath_serve_url(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL)));
                photoEntity.setDeviceId(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID)));
                items.add(photoEntity);
            }
            cursor.close();
        }catch (Exception e){}
        return items;
    }



    public  Object photosAll(Object object) {
        List<PhotoEntity> items = new ArrayList<>();
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID ,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID
            };




            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_SYNC + " = ?";
            String[] selectionArgs = { ((String) object) };

            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT+ " DESC";

            Cursor cursor = db.query(
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID)));
                photoEntity.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE)));
                photoEntity.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE)));
                photoEntity.setDelivery_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY)));
                photoEntity.setUri(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI)));
                photoEntity.setPic_uid(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID)));
                photoEntity.setType(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE)));
                photoEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT)));
                photoEntity.setUpdate_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT)));
                photoEntity.setPartners_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS)));
                photoEntity.setPath_serve_url(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL)));
                photoEntity.setDeviceId(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID)));



                items.add(photoEntity);
            }
            cursor.close();
        }catch (Exception e){}
        return items;
    }


    @Override
    public long save(Object object) {
        long insert=0;
       try {
           PhotoEntity photoEntity = (PhotoEntity) object;
           // Obtém o repositório de dados no modo de gravação
             db =  getWritableDatabase();

            // Crie um novo mapa de valores, onde os nomes das colunas são as chaves
           ContentValues values = new ContentValues();
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI ,photoEntity.getUri());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE ,photoEntity.getLatitude());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE ,photoEntity.getLongitude());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID ,photoEntity.getPic_uid());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE ,photoEntity.getType());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY ,photoEntity.getDelivery_id());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_SYNC ,photoEntity.getSync());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT ,photoEntity.getCreated_at());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS ,photoEntity.getPartners_id());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL ,photoEntity.getPath_serve_url());
           values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID ,photoEntity.getDeviceId());

           // Insira a nova linha, retornando o valor da chave primária da nova linha
           insert = db.insert(partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME, null, values);
       }catch (Exception e){}
       return insert;
    }

    @Override
    public int delete(Object object) {
        int deletedRows=0;
        try{
            // Define a parte 'onde' da consulta.
            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID + "=?";
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


    public int updatePhotoSyncNot(PhotoEntity photo) {
        int count=0;
        try{
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_SYNC ,"0");
            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID + "=?";
            String[] selectionArgs = {String.valueOf(photo.getId()) };
            count = db.update(
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
        }catch (Exception e){
            System.out.println("#4 "+e.getMessage());
        }
        return count;
    }


    public int photoSyncedOnServer(Object object) {
        int count=0;
        try{
            db = getWritableDatabase();
            PhotoEntity photoEntity = (PhotoEntity) object;
            // New value for one column
            String title = "MyNewTitle";
            ContentValues values = new ContentValues();

            values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_SYNC ,photoEntity.getSync());
            values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT , PartenerHelpes.getDate());
            values.put(  partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL , photoEntity.getPath_serve_url());


            // Which row to update, based on the title
            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID + "=?";
            String[] selectionArgs = {String.valueOf(photoEntity.getId()) };

              count = db.update(
                      partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);


        }catch (Exception e){

            System.out.println("#4 "+e.getMessage());
        }

        return count;
    }



    public Object havePhotosToSync(Object object) {
        List<PhotoEntity> items=new ArrayList<>() ;
        try{
            db = this.getReadableDatabase();
            // Define uma projeção que especifica quais colunas do banco de dados
            // você realmente usará após esta consulta.
            String[] projection = {
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID ,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL,
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID

            };
            // Filtra os resultados WHERE "title" = 'My Title'
            String selection = partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_SYNC + " = ?";
            String[] selectionArgs = { ((String) object) };
            // Como você deseja que os resultados sejam classificados no Cursor resultante
            String sortOrder =
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT+ " DESC";
            Cursor cursor = db.query(
                    partnersPhotosOperationContract.partnersPhotosOperationEntry.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    sortOrder               // The sort order
            );

            while(cursor.moveToNext()) {
                PhotoEntity photoEntity = new PhotoEntity();
                photoEntity.setId(cursor.getInt(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID)));
                photoEntity.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LONGITUDE)));
                photoEntity.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_LATITUE)));
                photoEntity.setDelivery_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_DELIVERY)));
                photoEntity.setUri(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_URI)));
                photoEntity.setPic_uid(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PIC_UID)));
                photoEntity.setType(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_TYPE)));
                photoEntity.setCreated_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_AT)));
                photoEntity.setUpdate_at(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_UPDATE_AT)));
                photoEntity.setPartners_id(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_ID_PARTNERS)));
                photoEntity.setPath_serve_url(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL)));
                photoEntity.setDeviceId(cursor.getString(cursor.getColumnIndexOrThrow(partnersPhotosOperationContract.partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID)));


                items.add(photoEntity);
            }
            cursor.close();
        }catch (Exception e){}
        return items;
    }



}
