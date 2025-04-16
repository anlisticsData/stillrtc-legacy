package br.com.stilldistribuidora.pco.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.stilldistribuidora.pco.config.Constante;

public class Conecao extends SQLiteOpenHelper {
    private static final String name = Constante.BASE_PCO;
    private static final int version = Constante.BASE_PCO_VERSAO;


    public Conecao(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("######## OOOO");


            String script_db_tbl = "";
            StringBuilder table = new StringBuilder();
            table.append(" CREATE TABLE IF NOT EXISTS  " + Constante.BASE_PCO_TABLE_AUTO_LOGIN + " (");
            table.append("user_login TEXT,");
            table.append("user_senha TEXT");
            table.append(");");
            db.execSQL(table.toString());

            script_db_tbl = " CREATE TABLE IF NOT EXISTS " + Constante.BASE_PCO_TABLE_WS_CONFIG + "("
                    + " " + Constante.WS_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " " + Constante.WS_DESCRICAO + " TEXT , "
                    + " " + Constante.WS_OPCAO + " TEXT )";
            db.execSQL(script_db_tbl);

            script_db_tbl = " CREATE TABLE IF NOT EXISTS " + Constante.BASE_PCO_TABLE_WS_MOVIMENTO + "("
                    + " " + Constante.WS_PCO_GRF_MV_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " " + Constante.WS_PCO_GRF_MV_MT_KEY + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_MT_NOME + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_MT_FORMATO + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_VM_VERSAO + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_EM_NOME + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_EM_IMG + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_CL_KEY + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_CL_NOME + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_QT_RT + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_DT_CREATE + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_MV_STATUS + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_DT_RT + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_MV_QT_RT + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_QT_ENTREGE + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_SINCRONIZADO + " TEXT )";
            db.execSQL(script_db_tbl);

            script_db_tbl = " CREATE TABLE IF NOT EXISTS " + Constante.BASE_PCO_TABLE_WS_MOVIMENTO_FOTO + "("
                    + " " + Constante.WS_PCO_GRF_MV_FT_CODIGO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " " + Constante.WS_PCO_GRF_MV_FT_CREATE_AT + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_PATH_FILE + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_NAME_FILE + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_TB_RETIRADA_GF_CODIGO + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_DEVICE + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_USER + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_LOC + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_SINCRONIZADA + " TEXT)";
            db.execSQL(script_db_tbl);

            script_db_tbl = " CREATE TABLE IF NOT EXISTS " + Constante.BASE_PCO_TABLE_WS_SINCRONIZAR + "("
                    + " " + Constante.WS_PCO_GRF_MV_FT_SINC_CODIGO + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " " + Constante.WS_PCO_GRF_MV_FT_SINC_OPERACAO + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_SINC_DT_CREATE + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_SINC_DT_FINISH + " TEXT , "
                    + " " + Constante.WS_PCO_GRF_MV_FT_SINC_STATUS + " TEXT)";
            db.execSQL(script_db_tbl);

            script_db_tbl = " CREATE TABLE IF NOT EXISTS " + Constante.DEVICE_TABLE_NAME + "("
                    + " " + Constante.DEVICE_STATUS_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " " + Constante.DEVICE_CODE_MOV_GRAFICA + " TEXT , "
                    + " " + Constante.DEVICE_LAT_LON + " TEXT , "
                    + " " + Constante.DEVICE_DEVICE_ID+ " TEXT , "
                    + " " + Constante.DEVICE_USER+ " TEXT , "
                    + " " + Constante.DEVICE_CREATE_AT+ " TEXT , "
                    + " " + Constante.DEVICE_STATUS+ " TEXT )";
            db.execSQL(script_db_tbl);

            script_db_tbl = " CREATE TABLE IF NOT EXISTS " + Constante.DOC_OPERACOES + "("
                + " " + Constante.DOC_OPERACOES_UUID + " TEXT PRIMARY KEY,"
                + " " + Constante.DOC_OPERACOES_UUID_LANCAMENTO+ " TEXT , "
                + " " + Constante.DOC_OPERACOES_STATUS+ " TEXT , "
                + " " + Constante.DOC_OPERACOES_DATA+ " TEXT , "
                + " " + Constante.DOC_OPERACOES_SINCRONED+ " TEXT , "
                + " " + Constante.DOC_OPERACOES_STRUCTS+ " TEXT )";
            db.execSQL(script_db_tbl);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constante.BASE_PCO_TABLE_AUTO_LOGIN);
        onCreate(db);
    }
}
