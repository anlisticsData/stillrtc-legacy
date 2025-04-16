package br.com.stilldistribuidora.partners.views.core.contract;

import android.provider.BaseColumns;

public class partnersOperationControlStateContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private partnersOperationControlStateContract() {}

    /* Inner class that defines the table contents */
    public static class partnersOperationControlStateContractEntry implements BaseColumns {
        public static final String TABLE_NAME = "partnersControlOperationState";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_ID_PARTNERS= "id_parteners";
        public static final String COLUMN_DELIVERI_ID = "delivery_fragment_id";
        public static final String COLUMN_ROUTER_ID= "route_id";

        public static final String COLUMN_TIME_CURRENT= "time_current";
        public static final String COLUMN_DELIVERI_START= "delivery_state";
        public static final String COLUMN_CREATED_AT = "created_at";
    }


    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + partnersOperationControlStateContractEntry.TABLE_NAME + " (" +
                    partnersOperationControlStateContractEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    partnersOperationControlStateContractEntry.COLUMN_DELIVERI_ID + " TEXT," +
                    partnersOperationControlStateContractEntry.COLUMN_ID_PARTNERS + " TEXT," +
                    partnersOperationControlStateContractEntry.COLUMN_ROUTER_ID + " TEXT," +
                    partnersOperationControlStateContractEntry.COLUMN_TIME_CURRENT + " TEXT," +
                    partnersOperationControlStateContractEntry.COLUMN_DELIVERI_START + " TEXT," +
                               partnersOperationControlStateContractEntry.COLUMN_CREATED_AT + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + partnersOperationControlStateContractEntry.TABLE_NAME;




}
