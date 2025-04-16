package br.com.stilldistribuidora.partners.Repository.Audio;

import android.provider.BaseColumns;

public class CabinAudioOperationCreateTable {

    public static class CabinAudioTableColumns implements BaseColumns {
        public static final String TABLE_NAME = "cabin_sounds_operation";
        public static final String TABLE_ID = "id";
        public static final String TABLE_CODE_DELIVERY = "code_delivery";
        public static final String TABLE_CODE_PARTNER = "code_partner";
        public static final String TABLE_AUDIO_PATH = "audio";
        public static final String TABLE_IS_AUDIO_CHECKED = "checked";

    }


    public static String create() {
        return "CREATE TABLE if not exists " + CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_NAME + " (\n" +
                "    " + CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    " + CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_DELIVERY + " TEXT NOT NULL,\n" +
                "    " + CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_CODE_PARTNER + " INTEGER  NULL,\n" +
                "    " + CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_AUDIO_PATH + " TEXT  NULL,\n" +
                "    " + CabinAudioOperationCreateTable.CabinAudioTableColumns.TABLE_IS_AUDIO_CHECKED + " TEXT  NULL\n" +
                ");" +

                "";
    }


    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AudioCreateTable.AudioTableColumns.TABLE_NAME;


}

