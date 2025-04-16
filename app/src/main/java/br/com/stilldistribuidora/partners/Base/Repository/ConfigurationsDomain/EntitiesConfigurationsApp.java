package br.com.stilldistribuidora.partners.Base.Repository.ConfigurationsDomain;

import android.provider.BaseColumns;

public class EntitiesConfigurationsApp {

    private EntitiesConfigurationsApp(){}

    public static class Config implements BaseColumns {
        public static final String TABLE_NAME = "configurations";
        public static final String COLUMN_ID= "id";
        public static final String COLUMN_UUID_CONFIG = "uuid_config";
        public static final String COLUMN_CONTENT = "content";
    }

}
