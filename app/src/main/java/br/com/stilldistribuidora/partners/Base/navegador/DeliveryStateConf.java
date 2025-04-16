package br.com.stilldistribuidora.partners.Base.navegador;

public class DeliveryStateConf {
    public static final String SQL_CREATE_ENTRIES = "" +
            "CREATE TABLE IF NOT EXISTS " + DeliveryState.DeliveryStateEntry.TABLE_NAME + " (\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_UUIDCOMPOSTA + "  INTEGER NOT NULL,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_DELIVERYID + "    INTEGER,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_ROUTERID + "      INTEGER,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_STOREID + "       INTEGER,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_PARTNERID + "     INTEGER,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_LATLNG + "  TEXT,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_NUVEM + "         INTEGER DEFAULT (0),\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_DEVICEID + "      INTEGER,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_BATERY + "        TEXT,\n" +
            "    " + DeliveryState.DeliveryStateEntry.COLUMN_NAME_LAPS + "        TEXT\n" +
            ");";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DeliveryState.DeliveryStateEntry.TABLE_NAME;
}

