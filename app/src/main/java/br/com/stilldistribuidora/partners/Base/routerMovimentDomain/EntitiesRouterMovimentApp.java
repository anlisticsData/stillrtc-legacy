package br.com.stilldistribuidora.partners.Base.routerMovimentDomain;


import android.provider.BaseColumns;

public class EntitiesRouterMovimentApp {
    public static class Columns implements BaseColumns {
        public static final String TABLE_NAME = "moviment_user_in_router";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_KEYS= "id_keys";
        public static final String COLUMN_ACTIVE_GEO= "state_point";
        public static final String COLUMN_ACTIVE_SYNC= "sync";
        public static final String COLUMN_ACTIVE_STATE_ACTION= "action_";
        public static final String COLUMN_ACTIVE_DEVICE_ID= "device_id";
        public static final String COLUMN_ACTIVE_DELIVERY_ID= "delivery_id";
        public static final String COLUMN_ACTIVE_DELIVERY_LAP= "lap";


    }

}
