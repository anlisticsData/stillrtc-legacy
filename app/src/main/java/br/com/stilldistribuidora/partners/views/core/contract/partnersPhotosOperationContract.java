package br.com.stilldistribuidora.partners.views.core.contract;

import android.provider.BaseColumns;

public class partnersPhotosOperationContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private partnersPhotosOperationContract() {}

    /* Inner class that defines the table contents */
    public static class partnersPhotosOperationEntry implements BaseColumns {
        public static final String TABLE_NAME = "partnersPictures";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_URI = "uri";
        public static final String COLUMN_LATITUE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_PIC_UID = "pic_uid";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_DELIVERY = "delivery_id";
        public static final String COLUMN_ID_PARTNERS= "id_partners";
        public static final String COLUMN_SYNC= "sync";
        public static final String COLUMN_PATH_SERVER_URL = "path_imagens_save_api";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_UPDATE_AT = "update_at";
        public static final String COLUMN_CREATED_DEVICE_ID = "device_id";

    }


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + partnersPhotosOperationEntry.TABLE_NAME + " (" +
                    partnersPhotosOperationEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    partnersPhotosOperationEntry.COLUMN_ID_PARTNERS + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_URI + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_LATITUE + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_LONGITUDE + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_PIC_UID + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_TYPE + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_DELIVERY + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_SYNC + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_PATH_SERVER_URL + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_CREATED_AT + " TEXT," +
                    partnersPhotosOperationEntry.COLUMN_UPDATE_AT + " TEXT,"+
                    partnersPhotosOperationEntry.COLUMN_CREATED_DEVICE_ID + " TEXT)";





    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + partnersPhotosOperationEntry.TABLE_NAME;


}
