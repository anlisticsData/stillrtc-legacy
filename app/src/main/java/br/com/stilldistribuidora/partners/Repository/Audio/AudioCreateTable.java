package br.com.stilldistribuidora.partners.Repository.Audio;

import android.provider.BaseColumns;

import br.com.stilldistribuidora.partners.Repository.SqlCreateds.AppOpeationCreatedSql;

public class AudioCreateTable {

    public static class AudioTableColumns implements BaseColumns {
        public static final String TABLE_NAME = "moviments_sounds";
        public static final String TABLE_ID = "id";
        public static final String TABLE_CODE_DELIVERY = "code_delivery";
        public static final String TABLE_CODE_PARTNER = "code_partner";
        public static final String TABLE_AUDIO_PATH = "audio";
        public static final String TABLE_IS_AUDIO_TIME_DEVICE = "time_device";
        public static final String TABLE_IS_AUDIO_LAT_LNG = "lat_lng";
        public static final String TABLE_IS_AUDIO_CODE_ROUTER = "code_router";
        public static final String TABLE_IS_AUDIO_UPLOAD = "is_upload";

    }



    public static  String create(){
        return "CREATE TABLE if not exists "+ AudioCreateTable.AudioTableColumns.TABLE_NAME+" (\n" +
                "    "+ AudioCreateTable.AudioTableColumns.TABLE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    "+ AudioCreateTable.AudioTableColumns.TABLE_CODE_DELIVERY+" TEXT NOT NULL,\n" +
                 "    "+AudioCreateTable.AudioTableColumns.TABLE_CODE_PARTNER+" INTEGER  NULL,\n" +
                "    "+AudioCreateTable.AudioTableColumns.TABLE_AUDIO_PATH+" TEXT  NULL,\n" +
                "    "+AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_TIME_DEVICE+" TEXT  NULL,\n" +
                "    "+AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_LAT_LNG+" TEXT  NULL,\n" +
                "    "+AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_CODE_ROUTER+" TEXT  NULL,\n" +
                "    "+ AudioCreateTable.AudioTableColumns.TABLE_IS_AUDIO_UPLOAD+"  TEXT\n" +
                ");" +

                "";
    }


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AudioCreateTable.AudioTableColumns.TABLE_NAME;


}
