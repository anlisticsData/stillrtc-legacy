package br.com.stilldistribuidora.stillrtc.db.models;

import java.io.Serializable;

import br.com.stilldistribuidora.stillrtc.db.Constants;

public class Config implements Serializable {
    private int id;
    private String descricao;
    private String dataJson;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public String getDataJson() {
        return dataJson;
    }
    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }



    public static String[] columns = new String[]{
            Constants.CONFIG_ID,
            Constants.CONFIG_DESCRICAO,
            Constants.CONFIG_OPCAO
    };

    public static String script_db_tblConfig = " CREATE TABLE IF NOT EXISTS  " + Config.class.getSimpleName() + "("
            + " " + Constants.CONFIG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE,"
            + " " + Constants.CONFIG_DESCRICAO + " TEXT, "
            + " " + Constants.CONFIG_OPCAO + " TEXT)";


}
