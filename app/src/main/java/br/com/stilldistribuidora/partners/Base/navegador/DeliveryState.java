package br.com.stilldistribuidora.partners.Base.navegador;

import android.provider.BaseColumns;

public class DeliveryState {
    private DeliveryState(){}

    public static class DeliveryStateEntry implements BaseColumns {
        public static final String TABLE_NAME = "deliveryState";
        public static final String COLUMN_NAME_ID= "id";
        public static final String COLUMN_NAME_UUIDCOMPOSTA = "uuidcomposta";
        public static final String COLUMN_NAME_DELIVERYID = "deliveryid";
        public static final String COLUMN_NAME_ROUTERID = "routerid";
        public static final String COLUMN_NAME_STOREID = "storeid";
        public static final String COLUMN_NAME_LATLNG = "latlng";
        public static final String COLUMN_NAME_NUVEM = "nuvem";
        public static final String COLUMN_NAME_DEVICEID = "deviceid";
        public static final String COLUMN_NAME_BATERY = "batery";
        public static final String COLUMN_NAME_PARTNERID = "partnerid";

        public static final String COLUMN_NAME_LAPS = "lap";
    }
}

/*
*
* CREATE TABLE deliveryState (
    id           INTEGER PRIMARY KEY AUTOINCREMENT,
    uuidcomposta INTEGER NOT NULL,
    deliveryid   INTEGER,
    routerid     INTEGER,
    storeid      INTEGER,
    partnerid    INTEGER,
    latlng       TEXT,
    nuvem        INTEGER DEFAULT (0),
    deviceid     INTEGER,
    batery       TEXT
);
*
* */

