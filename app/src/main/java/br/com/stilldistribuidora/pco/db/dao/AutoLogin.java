package br.com.stilldistribuidora.pco.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import br.com.stilldistribuidora.pco.config.Constante;
import br.com.stilldistribuidora.pco.db.BaseConexao;
import br.com.stilldistribuidora.pco.db.Conecao;

public class AutoLogin extends BaseConexao {

    private Conecao conn;
    private SQLiteDatabase banco;

    public AutoLogin(Context context) {
        super(context);
        conn = new Conecao(context);

    }


    public Long insert(String login, String senha) {
        banco = conn.getWritableDatabase();
        ContentValues paramets = new ContentValues();
        paramets.put(Constante.BASE_PCO_COL_SAVE_LOGIN, login);
        paramets.put(Constante.BASE_PCO_COL_SAVE_LOGIN_SENHA, senha);
        long ist = banco.insert(Constante.BASE_PCO_TABLE_AUTO_LOGIN, null, paramets);

        return ist;


    }

    public String getSenha(String login) {
        String senha = "";
        banco = conn.getReadableDatabase();
        Cursor c = banco.query(Constante.BASE_PCO_TABLE_AUTO_LOGIN,
                null,Constante.BASE_PCO_COL_SAVE_LOGIN+"=?",
                new String[]{login},
                null,
                null,
                null,
                null);


        if (c.moveToFirst()) {
                senha = c.getString(1);
        }
        c.close();
        banco.close();

        return senha;
    }


}
