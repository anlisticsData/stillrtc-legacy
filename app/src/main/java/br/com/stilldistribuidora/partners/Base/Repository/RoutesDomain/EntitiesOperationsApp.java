package br.com.stilldistribuidora.partners.Base.Repository.RoutesDomain;

import android.provider.BaseColumns;

public class EntitiesOperationsApp {
    private EntitiesOperationsApp(){}
    public static class Operations implements BaseColumns {
        public static final String TABLE_NAME = "operations";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_CREATED_AT = "created_at";
        public static final String COLUMN_ID_ROTAS= "idrotas";
        public static final String COLUMN_ROUTES = "routes";
        public static final String COLUMN_ROUTES_KEYS = "keys_receive";
        public static final String COLUMN_ACTIVE_DEVICE_ID= "device_id";


    }
}
