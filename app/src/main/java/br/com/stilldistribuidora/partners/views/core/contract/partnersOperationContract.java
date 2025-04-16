package br.com.stilldistribuidora.partners.views.core.contract;

import android.provider.BaseColumns;

public class partnersOperationContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private partnersOperationContract() {}

    /* Inner class that defines the table contents */
    public static class partnersOperationContractEntry implements BaseColumns {
        public static final String TABLE_NAME = "operationPartnersMoviment";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_ID_PARTNERS= "id_parteners";
        public static final String COLUMN_DELIVERI_ID = "delivery_fragment_id";
        public static final String COLUMN_ROUTER_ID= "route_id";
        public static final String COLUMN_LATITUDE= "latitude";
        public static final String COLUMN_LONGITUDE= "longitude";
        public static final String COLUMN_SYNC = "sync";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_CREATED_DEVICE_ID = "device_id";

    }


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + partnersOperationContractEntry.TABLE_NAME + " (" +
                    partnersOperationContractEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    partnersOperationContractEntry.COLUMN_DELIVERI_ID + " TEXT," +
                    partnersOperationContractEntry.COLUMN_ID_PARTNERS + " TEXT," +
                    partnersOperationContractEntry.COLUMN_LATITUDE + " TEXT," +
                    partnersOperationContractEntry.COLUMN_LONGITUDE + " TEXT," +
                    partnersOperationContractEntry.COLUMN_ROUTER_ID + " TEXT," +
                    partnersOperationContractEntry.COLUMN_SYNC + " TEXT," +
                    partnersOperationContractEntry.COLUMN_CREATED_AT + " TEXT,"+
                    partnersOperationContractEntry.COLUMN_CREATED_DEVICE_ID + " TEXT)";



    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + partnersOperationContractEntry.TABLE_NAME;




}
