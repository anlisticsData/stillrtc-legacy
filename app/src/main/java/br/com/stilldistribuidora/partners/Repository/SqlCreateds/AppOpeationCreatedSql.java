package br.com.stilldistribuidora.partners.Repository.SqlCreateds;

import android.provider.BaseColumns;

public class AppOpeationCreatedSql {




    private AppOpeationCreatedSql() {}



     public static class AppOpeationCreatedSqlEntry implements BaseColumns {
        public static final String TABLE_NAME = "AppOperations";
        public static final String TABLE_ID = "id";
        public static final String TABLE_OPERATION_ID = "operationID";

        public static final String TABLE_OPERATION_TYPE = "typeOperation";

        public static final String TABLE_ROUTER_ID = "routerID";
        public static final String TABLE_STOREID = "storeID";
        public static final String TABLE_PARTERNID = "parternID";
        public static final String TABLE_STATE = "state";
        public static final String TABLE_DISTANCEKM = "distanceKm";
        public static final String TABLE_DISTANCEMM = "distanceMm";
        public static final String TABLE_ZONASJSON = "zonasJson";
        public static final String TABLE_ROUTERJSON = "routerJson";
        public static final String TABLE_ROUTERMAP = "routerMap";
        public static final String TABLE_STORENAME = "storename";
        public static final String TABLE_PONTSTART = "pontStart";
        public static final String TABLE_CREATED_AT = "createdAt";
        public static final String TABLE_UPDATE_AT = "updateAt";
        public static final String TABLE_NUVEM = "nuvem";
        public static final String TABLE_JUSTIFICATIONS ="justifications" ;
        public static final String TABLE_ACCEPT_OPERATION ="acception" ;
        public static final String TABLE_ACCEPT_OPERATION_UPDATE ="acceptionUpdate" ;

         public static final String TABLE_ROUTER_TYPE ="typeOperation" ;
     }


    public static  String create(){

        return "CREATE TABLE if not exists "+AppOpeationCreatedSqlEntry.TABLE_NAME+" (\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_OPERATION_ID+" INTEGER NOT NULL,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_OPERATION_TYPE+" INTEGER  NULL,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_ROUTER_ID+" INTEGER NOT NULL,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_STOREID+"  INTEGER NOT NULL,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_PARTERNID+"    INTEGER ,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_STATE+"   INTEGER NOT NULL,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_DISTANCEKM+"   TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_DISTANCEMM+"  TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_ZONASJSON+"   TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_ROUTERJSON+"   TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_ROUTERMAP+"   TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_STORENAME+"   TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_PONTSTART+"   TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_CREATED_AT+" TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_NUVEM+" TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_JUSTIFICATIONS+" TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION+" TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_UPDATE_AT+"  TEXT,\n" +
                "    "+AppOpeationCreatedSqlEntry.TABLE_ACCEPT_OPERATION_UPDATE+"  TEXT\n" +
                ");" +

                "";
    }


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AppOpeationCreatedSqlEntry.TABLE_NAME;







}
